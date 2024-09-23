package argonath.reflector.reader.filter;

import argonath.reflector.reader.filter.element.KeyFilter;
import argonath.reflector.reader.filter.element.KeySetFilter;
import argonath.reflector.reader.filter.index.IndexFilter;
import argonath.reflector.reader.filter.index.IndexRangeFilter;
import argonath.reflector.reader.filter.index.IndexSetFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FilterParser {
    private FilterParser() {
    }

    private static final String INDEX_RANGE_FILTER_SEPARATOR = ":";
    public static final String SET_FILTER_SEPARATOR = ",";


    public static Filter parse(String filterExpressionStr) {
        if (StringUtils.isEmpty(filterExpressionStr)) {
            return Filters.NO_FILTER;
        }

        // Trim before processing
        String expression = filterExpressionStr.trim();

        // Step 1: LOGICAL OPERATOR
        // Not Supported yet

        // Step 2: EXPRESSION OPERATOR
        int[] operatorIndices = Arrays.stream(Operator.values())
                .map(operator -> expression.indexOf(operator.operator()))
                .filter(index -> index > -1)
                .mapToInt(Integer::intValue)
                .toArray();

        if (operatorIndices.length > 1) {
            throw new IllegalArgumentException("Multiple operators are not supported in a single filter expression: " + filterExpressionStr);
        }

        if (operatorIndices.length == 0) {
            return processNoOperator(expression);
        } else {
            Operator operator = Operator.values()[operatorIndices[0]];
            return OpParser.process(expression, operator);
        }
    }

    private static Filter processNoOperator(String expression) {

        // if the expression is numeric, then instantiate an IndexFilter
        if (isNumber(expression)) {
            return new IndexFilter(Integer.parseInt(expression));
        }

        // Index Range Filter
        if (expression.contains(INDEX_RANGE_FILTER_SEPARATOR)) {
            return parseIndexRangeFilter(expression);
        }

        // Set Filter
        if (expression.contains(SET_FILTER_SEPARATOR)) {
            return parseSetFilter(expression);
        }

        // Key Filter
        return new KeyFilter(expression.trim());
    }


    private static IndexRangeFilter parseIndexRangeFilter(String expression) {
        String[] parts = expression.split(INDEX_RANGE_FILTER_SEPARATOR, 2);
        String leftPart = parts[0];
        String rightPart = StringUtils.isEmpty(parts[1]) ? null : parts[1].trim();

        // left part cannot be omitted
        if (leftPart == null) {
            throw new IllegalArgumentException("Invalid Index Range Filter: '" + expression + "': Left part is not provided");
        }

        // both parts must be numeric, otherwise range scan is not supported
        if (!isNumber(leftPart) || (StringUtils.isNotEmpty(rightPart) && !isNumber(rightPart))) {
            throw new IllegalArgumentException("Invalid Index Range Filter: '" + expression + "': Invalid Range Format");
        }

        if (rightPart == null) {
            // if the right part is empty, then instantiate with max=null
            Integer min = Integer.parseInt(leftPart);
            return new IndexRangeFilter(min, null);
        }
        Integer min = Integer.parseInt(leftPart);
        Integer max = Integer.parseInt(parts[1]);
        return new IndexRangeFilter(min, max);
    }

    private static Filter parseSetFilter(String expression) {
        String[] values = expression.split(SET_FILTER_SEPARATOR);
        // convert array of string values to set of integers
        Set<Integer> intValues = new HashSet<>();
        Set<String> strValues = new HashSet<>();

        boolean isNumber = true;

        // if all values are numbers, then instantiate an IndexSetFilter, otherwise instantiate a KeySetFilter
        for (int i = 0; i < values.length; i++) {
            String curVal = values[i].trim();
            if (!isNumber(curVal)) {
                isNumber = false;
                strValues.add(curVal);
            } else {
                intValues.add(Integer.parseInt(curVal));
            }
        }
        return isNumber ?
                new IndexSetFilter(intValues) :
                new KeySetFilter(new HashSet<>(Arrays.asList(values)));
    }

    private static boolean isNumber(String expression) {
        return expression.matches("\\d+");
    }

}
