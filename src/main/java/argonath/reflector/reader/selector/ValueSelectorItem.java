package argonath.reflector.reader.selector;

import argonath.reflector.reader.filter.Filter;
import argonath.reflector.reader.filter.FilterParser;
import argonath.reflector.reflection.ReflectiveAccessor;

/**
 * An element of the ValueSelector that corresponds to rules to access the respective member part of the object graph.
 * The ValueSelectorItem is composed of:
 * - fieldName: the name of the element (in Object graph corresponds to a variable name)
 * - next: the next element in the expression
 * - filterSelector: the expression that filters iterable type elements (within brackets to filter-out iterable type elements)
 * - valueMapper: the expression that maps the resolved object value to a custom evaluated value
 */
public class ValueSelectorItem {

    /**
     * The name of the element (in Object graph corresponds to a variable name)
     */
    private String fieldName;

    /**
     * The filter expression (within brackets to filter-out iterable type elements)
     */
    private String filterExpressionStr;

    /**
     * The filter resolved from the filterSelector (lazy loaded)
     */
    private Filter filter;

    /**
     * The expression that maps the resolved object value to a custom evaluated value
     */
    private String valueMapperStr;

    /**
     * The Value Mapper resolved from the valueMapperExpression (lazy loaded)
     */
    private ValueMapper valueMapper;

    /**
     * Next element in the XPath expression
     */
    private ValueSelectorItem next;

    ValueSelectorItem(ValueSelectorItem prev, String elementStr) {
        parse(elementStr);
        if (prev != null) {
            prev.next = this;
        }
    }

    private void parse(String elementStr) {
        int start = elementStr.indexOf('[');
        int end = elementStr.lastIndexOf(']');
        if ((start > -1 && end == -1) || (start == -1 && end > -1) || (start > -1 && end > -1 && end <= start)) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (brackets are not balanced)");
        }
        boolean hasInstanceSelectorExpr = start > -1 && end > -1;
        if (hasInstanceSelectorExpr) {
            // text within brackets = instance selection expression
            filterExpressionStr = elementStr.substring(start + 1, end);
        }

        // find the first instance of '.'
        int vmIndex = elementStr.indexOf('.');
        boolean hasValueMapper = vmIndex > -1;
        if (hasValueMapper) {
            // text after '.' = value mapper expression
            valueMapperStr = elementStr.substring(vmIndex + 1);
            valueMapper = ValueMapperParser.parse(valueMapperStr);
        } else {
            valueMapper = ValueMappers.IDENTITY;
        }

        fieldName = hasValueMapper || hasInstanceSelectorExpr ?
                elementStr.substring(0, minIndex(start, vmIndex)) :
                elementStr;

        // Validations
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (name is empty)");
        }

        // Value Mapper Expression must not be empty
        if (hasValueMapper && valueMapperStr.isEmpty()) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (value mapper is empty)");
        }

        // Instance Selector Expression must not be empty
        if (hasInstanceSelectorExpr && filterExpressionStr.isEmpty()) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (instance selector is empty)");
        }

        // Instantiate Filter
        filter = FilterParser.parse(filterExpressionStr);
    }

    private int minIndex(int start, int vmIndex) {
        if (start == -1) {
            return vmIndex;
        } else if (vmIndex == -1) {
            return start;
        } else {
            return Math.min(start, vmIndex);
        }
    }

    public ValueMapper valueMapper() {
        return valueMapper;
    }

    public Filter filter() {
        return filter;
    }

    public ValueSelectorItem next() {
        return next;
    }

    public Object extract(Object obj) {
        return ReflectiveAccessor.getFieldValue(obj, fieldName);
    }

    public String fieldName() {
        return fieldName;
    }
}
