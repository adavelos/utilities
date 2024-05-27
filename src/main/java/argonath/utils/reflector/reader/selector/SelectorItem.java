package argonath.utils.reflector.reader.selector;

import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.reflector.reader.filter.Filter;
import argonath.utils.reflector.reader.filter.Filters;
import argonath.utils.reflector.reader.mapper.ValueMapper;
import argonath.utils.reflector.reader.mapper.ValueMappers;

/**
 * An element of the Selector that corresponds to rules to access the respective member part of the object graph.
 * The SelectorItem is composed of:
 * - name: the name of the element (in Object graph corresponds to a variable name)
 * - next: the next element in the expression
 * - filterSelector: the expression that filters iterable type elements (within brackets to filter-out iterable type elements)
 * - valueMapper: the expression that maps the extracted object value to a custom evaluated value
 */
public class SelectorItem {

    /**
     * The name of the element (in Object graph corresponds to a variable name)
     */
    private String fieldName;

    /**
     * The filter expression (within brackets to filter-out iterable type elements)
     */
    private String filterExpression;

    /**
     * The filter resolved from the filterSelector (lazy loaded)
     */
    private Filter filter;

    /**
     * The expression that maps the extracted object value to a custom evaluated value
     */
    private String valueMapperExpression;

    /**
     * The Value Mapper resolved from the valueMapperExpression (lazy loaded)
     */
    private ValueMapper valueMapper;

    /**
     * Next element in the XPath expression
     */
    private SelectorItem next;

    SelectorItem(SelectorItem prev, String elementStr) {
        parse(elementStr);
        if (prev != null) {
            prev.next = this;
        }
    }

    private void parse(String elementStr) {
        int start = elementStr.indexOf('[');
        int end = elementStr.lastIndexOf(']');
        boolean hasInstanceSelectorExpr = start > -1 && end > -1;
        if (hasInstanceSelectorExpr) {
            // text within brackets = instance selection expression
            filterExpression = elementStr.substring(start + 1, end - 1);
        }

        // find the first instance of '@'
        int vmIndex = elementStr.indexOf('@');
        boolean hasValueMapper = vmIndex > -1;
        if (hasValueMapper) {
            // text after '@' = value mapper expression
            valueMapperExpression = elementStr.substring(vmIndex + 1);
        }

        // text before '[' or '@' = name
        fieldName = hasValueMapper || hasInstanceSelectorExpr ?
                elementStr.substring(0, Math.min(start, vmIndex)) :
                elementStr;

        // Validations
        if (fieldName.isEmpty()) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (name is empty)");
        }

        // Value Mapper Expression must not be empty
        if (hasValueMapper && valueMapperExpression.isEmpty()) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (value mapper is empty)");
        }

        // Instance Selector Expression must not be empty
        if (hasInstanceSelectorExpr && filterExpression.isEmpty()) {
            throw new IllegalArgumentException("Invalid XPath element: " + elementStr + " (instance selector is empty)");
        }
    }

    public ValueMapper valueMapper() {
        if (valueMapper != null) {
            return valueMapper;
        }
        valueMapper = ValueMappers.of(valueMapperExpression);
        return valueMapper;
    }

    public Filter filter() {
        if (filter == null) {
            filter = Filters.of(filterExpression);
        }
        return filter;
    }

    public SelectorItem next() {
        return next;
    }

    public Object extract(Object obj) {
        return ReflectiveAccessor.getFieldValue(obj, fieldName);
    }

    public String fieldName() {
        return fieldName;
    }
}
