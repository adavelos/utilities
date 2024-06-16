package argonath.utils.reflector.reader.expression;

import java.util.regex.Pattern;

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

    Pattern pattern() {
        return pattern;
    }
}
