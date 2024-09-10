package argonath.reflector.generator;

import argonath.utils.WcMatcher;

public class FieldSelector {

    enum Type {
        PATH, FIELD_NAME, FIELD_NAME_WC, TYPE
    }

    private String expression;

    private Class<?> clazz;

    private Type type;

    private FieldSelector(String expression, Class<?> clazz, Type type) {
        this.expression = expression;
        this.clazz = clazz;
        this.type = type;
    }

    public static FieldSelector ofType(Class<?> type) {
        return new FieldSelector(null, type, Type.TYPE);
    }

    public static FieldSelector ofPath(String expression) {
        return new FieldSelector(expression, null, Type.PATH);
    }

    public static FieldSelector ofFieldName(String expression) {
        if (WcMatcher.isWildcard(expression)) {
            return new FieldSelector(expression, null, Type.FIELD_NAME_WC);
        }
        return new FieldSelector(expression, null, Type.FIELD_NAME);
    }

    public Type type() {
        return type;
    }

    public String expression() {
        return expression;
    }

    public Class<?> clazz() {
        return clazz;
    }

public boolean isPath() {
        return type == Type.PATH;
    }
}
