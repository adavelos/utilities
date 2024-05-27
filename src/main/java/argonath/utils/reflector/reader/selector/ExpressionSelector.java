//package argonath.utils.reflector.reader.selector;
//
//import argonath.utils.xpath.XPathUtil;
//
//import java.util.regex.Matcher;
//
//public class ExpressionSelector {
//    private Selector.Token left;
//    private Operator operator;
//    private Selector.Token right;
//
//    static ExpressionSelector parse(String expression) {
//        for (Operator operator : Operator.values()) {
//            Matcher matcher = operator.pattern.matcher(expression);
//            if (matcher.matches()) {
//                return new ExpressionSelector(matcher.group(1), operator, matcher.group(2));
//            }
//        }
//        throw new IllegalArgumentException("Invalid expression: " + expression);
//    }
//
//    private ExpressionSelector(String left, Operator operator, String right) {
//        this.left = parseToken(XPathUtil.stripQuotes(left));
//        this.operator = operator;
//        this.right = parseToken(XPathUtil.stripQuotes(right));
//    }
//
//    private Selector.Token parseToken(String tokenStr) {
//        Method method = Method.parse(tokenStr);
//        Selector.Token token = method != null ?
//                new Selector.MethodToken(method) :
//                new Selector.ValueToken(tokenStr);
//        return token;
//    }
//
//    public Selector.Token left() {
//        return left;
//    }
//
//    public Operator operator() {
//        return operator;
//    }
//
//    public Selector.Token right() {
//        return right;
//    }
//
//}
//}
