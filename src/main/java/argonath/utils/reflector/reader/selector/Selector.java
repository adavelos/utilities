package argonath.utils.reflector.reader.selector;

import argonath.utils.Assert;
import argonath.utils.xpath.XPathUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Selector for selecting information through Object Instance Graphs using XPath-like expressions.
 * <p>
 * The Instance Selector contains expressions to filter items within the object instance and map selected values.
 */
public class Selector {
    private List<SelectorItem> elements;

    private String expression;

    private Selector(String expression, List<SelectorItem> elements) {
        this.expression = expression;
        this.elements = elements;
    }

    /**
     * Parses the given X-Path like expression into an Instance Selector
     */
    public static Selector parse(String expression) {
        Assert.isTrue(XPathUtil.isValid(expression), "Invalid Selector Expression: " + expression);
        String canonicalPath = XPathUtil.canonicalPath(expression);

        List<String> elements = XPathUtil.split(canonicalPath);
        SelectorItem previous = null;
        List<SelectorItem> pathElements = new ArrayList<>();
        for (String element : elements) {
            SelectorItem next = new SelectorItem(previous, element);
            pathElements.add(next);
            previous = next;
        }

        return new Selector(expression, pathElements);
    }

    public List<SelectorItem> elements() {
        return elements;
    }

    public SelectorItem first() {
        return elements.isEmpty() ? null : elements.get(0);
    }

    @Override
    public String toString() {
        return expression;
    }


}
