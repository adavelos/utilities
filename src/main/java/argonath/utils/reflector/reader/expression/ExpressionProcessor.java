package argonath.utils.reflector.reader.expression;

import argonath.utils.xpath.XPathUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExpressionProcessor {

    public static Expression parse(String expressionStr) {
        if (expressionStr == null) {
            return null;
        }

        // TODO: support multiple operators
        int equalsIndex = expressionStr.indexOf("=");

        // Simple Expression
        boolean isSimple = equalsIndex == -1;
        if (isSimple) {
            return Expression.simpleExpression(expressionStr, parseOperand(expressionStr));
        }

        // Validation of the expression
        String leftPart = expressionStr.substring(0, equalsIndex);
        String rightPart = expressionStr.substring(equalsIndex + 1);
        if (!isSimple && StringUtils.isBlank(rightPart)) {
            throw new IllegalArgumentException("Invalid expression: " + expressionStr);
        }

        // Complex Expression
        Operand leftOperand = parseOperand(leftPart);
        Operand rightOperand = parseOperand(rightPart);
        Operator operator = Operator.EQUALS;
        return Expression.expression(expressionStr, leftOperand, rightOperand, operator);
    }

    public static Operand parseOperand(String operandStr) {
        Method method = parseMethod(operandStr);
        if (method == null) {
            return Operand.fixedValue(operandStr);
        }
        return Operand.method(method);
    }

    public static Method parseMethod(String methodStr) {
        int openBracket = methodStr.indexOf("(");
        int closeBracket = methodStr.lastIndexOf(")");
        if (openBracket == -1 || closeBracket == -1) {
            return null;
        }
        String methodName = methodStr.substring(0, openBracket);
        String argsStr = methodStr.substring(openBracket + 1, closeBracket);
        String[] split = argsStr.split(",", -1);
        List<String> args = Arrays.asList(split).stream()
                .map(String::trim)
                .map(XPathUtil::stripQuotes)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        return new Method(methodName, args, false);
    }
}
