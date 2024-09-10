package argonath.reflector.reader.selector;

import argonath.utils.Assert;
import argonath.utils.XPathUtil;

import java.util.ArrayList;
import java.util.List;

public class ValueSelector {

    private List<ValueSelectorItem> elements;

    private String expression;

    private ValueSelector(String expression, List<ValueSelectorItem> elements) {
        this.expression = expression;
        this.elements = elements;
    }

    public static ValueSelector parse(String expression) {
        Assert.isTrue(XPathUtil.isValid(expression), "Invalid ValueSelector Expression: " + expression);
        String canonicalPath = XPathUtil.canonicalPath(expression);

        List<String> elements = XPathUtil.split(canonicalPath);
        ValueSelectorItem previous = null;
        List<ValueSelectorItem> pathElements = new ArrayList<>();
        for (String element : elements) {
            ValueSelectorItem next = new ValueSelectorItem(previous, element);
            pathElements.add(next);
            previous = next;
        }
        return new ValueSelector(expression, pathElements);
    }

    public List<ValueSelectorItem> elements() {
        return elements;
    }

    public ValueSelectorItem first() {
        return elements.isEmpty() ? null : elements.get(0);
    }

    @Override
    public String toString() {
        return expression;
    }
}