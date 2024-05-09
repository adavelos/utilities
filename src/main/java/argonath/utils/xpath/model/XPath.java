package argonath.utils.xpath.model;

import argonath.utils.Assert;
import argonath.utils.xpath.XPathUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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


    public interface Selector {
    }

    public static class ValueSelector implements Selector {
        private String value;

        public ValueSelector(String value) {
            this.value = value;
        }

        public String value() {
            return value;
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
                    new LiteralToken(tokenStr);
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
    }

    public interface Token {
    }

    public static class LiteralToken implements Token {
        private String value;

        public LiteralToken(String value) {
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
