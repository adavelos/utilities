package argonath.utils.xpath.model;

import argonath.utils.Assert;
import argonath.utils.xpath.ExpressionUtil;
import argonath.utils.xpath.XPathUtil;
import org.apache.commons.lang3.tuple.Pair;

public class XPathElement {

    /**
     * The name of the element (in Object graph corresponds to a variable name)
     */
    private String name;

    /**
     * There are two selector types:
     * - single values (can be a number, a string, etc.)
     * (values are interpreted differently depending on the context: a number in a List is an index, in a Map it's a key, etc.)
     * - expressions (e.g. [id=c1], [index()<3], [text()!='foo'], etc.)
     */
    private XPath.Selector selector;

    static XPathElement parse(String elementStr) {
        Pair<String, String> parsedElement = ExpressionUtil.parseBrackets(elementStr);
        return new XPathElement(parsedElement.getLeft(), parseSelector(parsedElement.getRight()));
    }

    private static XPath.Selector parseSelector(String selectorExpr) {
        if (selectorExpr == null) {
            return null;
        }
        XPath.Selector ret;
        try {
            ret = XPath.ExpressionSelector.parse(selectorExpr);

        } catch (IllegalArgumentException e) {
            ret = new XPath.ValueSelector(selectorExpr);
        }
        return ret;
    }

    private XPathElement(String name, XPath.Selector selector) {
        this.name = name;
        this.selector = selector;
    }

    public String name() {
        return name;
    }

    public XPath.Selector selector() {
        return selector;
    }
}
