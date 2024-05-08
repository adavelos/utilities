package argonath.utils.test;


import argonath.utils.Assert;
import argonath.utils.XPath;
import org.junit.jupiter.api.Test;

public class TestXPath {

    @Test
    public void testValueXPath() {
        String xpath = "/root/element1/element2";
        XPath xPath = XPath.parse(xpath);
        Assert.isTrue(xPath.elements().size() == 3, "XPath Parse - Element Count");
        Assert.isTrue(xPath.elements().get(0).name().equals("root"), "XPath Parse - Element 1");
        Assert.isTrue(xPath.elements().get(1).name().equals("element1"), "XPath Parse - Element 2");
        Assert.isTrue(xPath.elements().get(2).name().equals("element2"), "XPath Parse - Element 3");

        // Expression Selector
        String xpath2 = "/root/element1/element2[attr1='value1']";
        XPath xPath2 = XPath.parse(xpath2);
        Assert.isTrue(xPath2.elements().size() == 3, "XPath Parse - Element Count 2");
        Assert.isTrue(xPath2.elements().get(0).name().equals("root"), "XPath Parse - Element 1 2");
        Assert.isTrue(xPath2.elements().get(1).name().equals("element1"), "XPath Parse - Element 2 2");
        Assert.isTrue(xPath2.elements().get(2).name().equals("element2"), "XPath Parse - Element 3 2");
        XPath.Selector selector = xPath2.elements().get(2).selector();
        Assert.isTrue(selector instanceof XPath.ExpressionSelector, "XPath Parse - Selector Type");
        if (selector instanceof XPath.ExpressionSelector expressionSelector) {
            XPath.Token left = expressionSelector.left();
            Assert.isTrue(left instanceof XPath.LiteralToken, "XPath Parse - Selector Left Type");
            if (left instanceof XPath.LiteralToken literalToken) {
                Assert.isTrue(literalToken.value().equals("attr1"), "XPath Parse - Selector Left Value");
            }
            XPath.Token right = expressionSelector.right();
            Assert.isTrue(right instanceof XPath.LiteralToken, "XPath Parse - Selector Right Type");
            if (right instanceof XPath.LiteralToken literalToken) {
                Assert.isTrue(literalToken.value().equals("value1"), "XPath Parse - Selector Right Value");
            }
            Assert.isTrue(expressionSelector.operator() == XPath.Operator.EQUALS, "XPath Parse - Selector Operator");
        }

        // Value Selector
        String xpath3 = "/root/element1/element2[value]";
        XPath xPath3 = XPath.parse(xpath3);
        Assert.isTrue(xPath3.elements().size() == 3, "XPath Parse - Element Count 3");
        Assert.isTrue(xPath3.elements().get(0).name().equals("root"), "XPath Parse - Element 1 3");
        Assert.isTrue(xPath3.elements().get(1).name().equals("element1"), "XPath Parse - Element 2 3");
        Assert.isTrue(xPath3.elements().get(2).name().equals("element2"), "XPath Parse - Element 3 3");
        XPath.Selector selector2 = xPath3.elements().get(2).selector();
        Assert.isTrue(selector2 instanceof XPath.ValueSelector, "XPath Parse - Selector Type 2");
        if (selector2 instanceof XPath.ValueSelector valueSelector) {
            Assert.isTrue(valueSelector.value().equals("value"), "XPath Parse - Selector Value");
        }

        // Expression Selector with Method Token
        String xpath4 = "/root/element1/element2[index()<3]";
        XPath xPath4 = XPath.parse(xpath4);
        Assert.isTrue(xPath4.elements().size() == 3, "XPath Parse - Element Count 4");
        Assert.isTrue(xPath4.elements().get(0).name().equals("root"), "XPath Parse - Element 1 4");
        Assert.isTrue(xPath4.elements().get(1).name().equals("element1"), "XPath Parse - Element 2 4");
        Assert.isTrue(xPath4.elements().get(2).name().equals("element2"), "XPath Parse - Element 3 4");
        XPath.Selector selector3 = xPath4.elements().get(2).selector();
        Assert.isTrue(selector3 instanceof XPath.ExpressionSelector, "XPath Parse - Selector Type 3");
        if (selector3 instanceof XPath.ExpressionSelector expressionSelector) {
            XPath.Token left = expressionSelector.left();
            Assert.isTrue(left instanceof XPath.MethodToken, "XPath Parse - Selector Left Type 3");
            if (left instanceof XPath.MethodToken methodToken) {
                Assert.isTrue(methodToken.method().name().equals("index"), "XPath Parse - Selector Left Value 3");
                Assert.isTrue(methodToken.method().args().size() == 0, "XPath Parse - Selector Left Arguments 3");
            }
            XPath.Token right = expressionSelector.right();
            Assert.isTrue(right instanceof XPath.LiteralToken, "XPath Parse - Selector Right Type 3");
            if (right instanceof XPath.LiteralToken literalToken) {
                Assert.isTrue(literalToken.value().equals("3"), "XPath Parse - Selector Right Value 3");
            }
            Assert.isTrue(expressionSelector.operator() == XPath.Operator.LESS_THAN, "XPath Parse - Selector Operator 3");
        }

    }
}
