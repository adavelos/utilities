package argonath.utils.reflector.reader.expression;

public class Expression {
    String value;
    Operand leftOperand;
    Operand rightOperand;
    Operator operator;

    private Expression(String value, Operand leftOperand, Operand rightOperand, Operator operator) {
        this.value = value;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }

    public static Expression simpleExpression(String value, Operand operand) {
        return new Expression(value, operand, null, null);
    }

    public static Expression expression(String value, Operand leftOperand, Operand rightOperand, Operator operator) {
        return new Expression(value, leftOperand, rightOperand, operator);
    }

    public static Expression fixedValue(String value) {
        return new Expression(value, Operand.fixedValue(value), null, null);
    }

    public boolean isSimple() {
        return this.leftOperand != null && this.rightOperand == null && this.operator == null;
    }

    // is fixed
    public boolean isFixed() {
        return this.leftOperand != null && this.rightOperand == null && this.operator == null && this.leftOperand.isFixedValue();
    }

    public String value() {
        return this.value;
    }

    public Operand leftOperand() {
        return this.leftOperand;
    }

    public Operand rightOperand() {
        return this.rightOperand;
    }

    public Operator operator() {
        return this.operator;
    }
}
