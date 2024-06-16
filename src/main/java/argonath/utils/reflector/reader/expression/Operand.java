package argonath.utils.reflector.reader.expression;

public class Operand {
    private String fixedValue;
    private Method method;

    private Operand(String fixedValue, Method method) {
        this.fixedValue = fixedValue;
        this.method = method;
    }

    public static Operand fixedValue(String value) {
        return new Operand(value, null);
    }

    public static Operand method(Method method) {
        return new Operand(null, method);
    }

    public static Operand expression(Expression expression) {
        return new Operand(null, null);
    }

    public String fixedValue() {
        return this.fixedValue;
    }

    public Method method() {
        return this.method;
    }

    public boolean isFixedValue() {
        return this.fixedValue != null;
    }

    public boolean isMethod() {
        return this.method != null;
    }

}
