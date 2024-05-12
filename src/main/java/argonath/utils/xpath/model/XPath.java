package argonath.utils.xpath.model;

import argonath.utils.Assert;
import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.xpath.XPathUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * XPath expression is used against:
 * - Classes: then the elements of the expression are simple and do not contain any selector
 * - Object Instances: then selectors can be used to filter the field elements
 */
public class XPath {
    private List<XPathElement> elements;

    public XPath(List<XPathElement> elements) {
        this.elements = elements;
    }

    public static XPath parse(String path) {
        Assert.isTrue(XPathUtil.isValid(path), "Invalid XPath: " + path);
        String canonicalPath = XPathUtil.canonicalPath(path);
        List<XPathElement> elements = XPathUtil.split(canonicalPath).stream()
                .map(XPathElement::parse)
                .collect(Collectors.toList());
        return new XPath(elements);
    }

    public List<XPathElement> elements() {
        return elements;
    }

    /**
     * Returns the XPath by skipping the top 'level' levels
     */
    public XPath subpath(int level) {
        return subpath(level, elements.size());
    }

    /**
     * Returns the XPath by skipping the top 'from' levels and keeping until 'to' level
     */
    public XPath subpath(int from, int to) {
        // range checks
        Assert.isTrue(from >= 0, "Invalid from: " + from);
        Assert.isTrue(to >= 0, "Invalid to: " + to);
        Assert.isTrue(from <= to, "Invalid range: " + from + " to " + to);
        Assert.isTrue(to <= elements.size(), "Invalid to: " + to);
        return new XPath(elements.subList(from, to));
    }

    /**
     * Returns the top level XPath
     */
    public XPath top() {
        return new XPath(elements.subList(0, 1));
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public String toString() {
        List<String> elementStr = elements.stream()
                .map(XPathElement::expressionString)
                .collect(Collectors.toList());
        return XPathUtil.combine(elementStr);
    }

    public XPathElement singleElement() {
        Assert.isTrue(elements.size() == 1, "Single element expected, but found: " + elements.size());
        return elements.get(0);
    }

    public interface Selector {
        Object apply(Object object);
    }

    public static class IdentitySelector implements Selector {
        public Object apply(Object object) {
            return object;
        }
    }

    public static class ValueSelector implements Selector {
        private String value;

        public ValueSelector(String value) {
            this.value = value;
        }

        /**
         * Applicable for:
         * - List of simple types (String, Integer, etc.): match element value
         * - Maps: match key
         */
        public String value() {
            return value;
        }

        /**
         * For index selectors return an integer (to be matched against a list element).
         */
        public Integer index() {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Selector cannot be used to select an element index: " + value);
            }
        }

        @Override
        public Object apply(Object object) {
            throw new UnsupportedOperationException("ValueSelector cannot be applied");
        }
    }

    /**
     * An expression selector is composed by an operator and two matcher tokens.
     * A token can be either a literal value or a method. In the left token, a literal is a pointer to a relative path or can be a method that evaluates to an intrinsic property (i.e. index()).
     * In the left part, a literal value is a constant value that is matching to the respective element value (i.e. the value of a variable in Object Graph)
     * <p>
     * Example: [id='c1'], [index()<3], [text()!='foo']
     */
    public static class ExpressionSelector implements Selector {

        private Token left;
        private Operator operator;
        private Token right;

        static ExpressionSelector parse(String expression) {
            for (Operator operator : Operator.values()) {
                Matcher matcher = operator.pattern.matcher(expression);
                if (matcher.matches()) {
                    return new ExpressionSelector(matcher.group(1), operator, matcher.group(2));
                }
            }
            throw new IllegalArgumentException("Invalid expression: " + expression);
        }

        private ExpressionSelector(String left, Operator operator, String right) {
            this.left = parseToken(XPathUtil.stripQuotes(left));
            this.operator = operator;
            this.right = parseToken(XPathUtil.stripQuotes(right));
        }

        private Token parseToken(String tokenStr) {
            Method method = Method.parse(tokenStr);
            Token token = method != null ?
                    new MethodToken(method) :
                    new ValueToken(tokenStr);
            return token;
        }

        public Token left() {
            return left;
        }

        public Operator operator() {
            return operator;
        }

        public Token right() {
            return right;
        }

        @Override
        public Object apply(Object object) {
            throw new UnsupportedOperationException("ExpressionSelector cannot be applied");
        }
    }

    public interface Token {
    }

    public static class ValueToken implements Token {
        private String value;

        public ValueToken(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    public static class MethodToken implements Token {
        private Method method;

        public MethodToken(Method method) {
            this.method = method;
        }

        public Method method() {
            return method;
        }
    }

    /**
     * Operators are used to match two values that resolve from ExpressionSelector tokens
     */
    public enum Operator {
        EQUALS("="),
        NOT_EQUALS("!="),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        GREATER_THAN_OR_EQUALS(">="),
        LESS_THAN_OR_EQUALS("<=");

        private String operator;

        private Pattern pattern;

        Operator(String operator) {
            this.operator = operator;
            this.pattern = Pattern.compile("(.*)" + operator + "(.*)");
        }
    }

}
