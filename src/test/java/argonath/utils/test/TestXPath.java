package argonath.utils.test;


import argonath.utils.xpath.model.XPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestXPath {

    @Test
    public void testValueXPath() {
        String xpath = "/root/element1/element2";
        XPath xPath = XPath.parse(xpath);
        Assertions.assertEquals(3, xPath.elements().size(), "XPath Parse - Element Count");
        Assertions.assertEquals("root", xPath.elements().get(0).name(), "XPath Parse - Element 1");
        Assertions.assertEquals("element1", xPath.elements().get(1).name(), "XPath Parse - Element 2");
        Assertions.assertEquals("element2", xPath.elements().get(2).name(), "XPath Parse - Element 3");

        // Expression Selector
        String xpath2 = "/root/element1/element2[attr1='value1']";
        XPath xPath2 = XPath.parse(xpath2);
        Assertions.assertEquals(3, xPath2.elements().size(), "XPath Parse - Element Count 2");
        Assertions.assertEquals("root", xPath2.elements().get(0).name(), "XPath Parse - Element 1 2");
        Assertions.assertEquals("element1", xPath2.elements().get(1).name(), "XPath Parse - Element 2 2");
        Assertions.assertEquals("element2", xPath2.elements().get(2).name(), "XPath Parse - Element 3 2");
        XPath.Selector selector = xPath2.elements().get(2).selector();
        Assertions.assertTrue(selector instanceof XPath.ExpressionSelector, "XPath Parse - Selector Type");
        XPath.ExpressionSelector expressionSelector = (XPath.ExpressionSelector) selector;
        {
            XPath.Token left = expressionSelector.left();
            Assertions.assertTrue(left instanceof XPath.ValueToken, "XPath Parse - Selector Left Type");
            XPath.ValueToken literalToken = (XPath.ValueToken) left;
            Assertions.assertEquals("attr1", literalToken.value(), "XPath Parse - Selector Left Value");
            XPath.Token right = expressionSelector.right();
            Assertions.assertTrue(right instanceof XPath.ValueToken, "XPath Parse - Selector Right Type");
            XPath.ValueToken literalToken2 = (XPath.ValueToken) right;
            Assertions.assertEquals("value1", literalToken2.value(), "XPath Parse - Selector Right Value");
            Assertions.assertSame(expressionSelector.operator(), XPath.Operator.EQUALS, "XPath Parse - Selector Operator");
        }


        // Value Selector
        String xpath3 = "/root/element1/element2[value]";
        XPath xPath3 = XPath.parse(xpath3);
        Assertions.assertEquals(3, xPath3.elements().size(), "XPath Parse - Element Count 3");
        Assertions.assertEquals("root", xPath3.elements().get(0).name(), "XPath Parse - Element 1 3");
        Assertions.assertEquals("element1", xPath3.elements().get(1).name(), "XPath Parse - Element 2 3");
        Assertions.assertEquals("element2", xPath3.elements().get(2).name(), "XPath Parse - Element 3 3");
        XPath.Selector selector2 = xPath3.elements().get(2).selector();
        Assertions.assertTrue(selector2 instanceof XPath.ValueSelector, "XPath Parse - Selector Type 2");
        XPath.ValueSelector valueSelector = (XPath.ValueSelector) selector2;
        Assertions.assertEquals("value", valueSelector.value(), "XPath Parse - Selector Value");

        // Expression Selector with Method Token
        String xpath4 = "/root/element1/element2[index()<3]";
        XPath xPath4 = XPath.parse(xpath4);
        Assertions.assertEquals(3, xPath4.elements().size(), "XPath Parse - Element Count 4");
        Assertions.assertEquals("root", xPath4.elements().get(0).name(), "XPath Parse - Element 1 4");
        Assertions.assertEquals("element1", xPath4.elements().get(1).name(), "XPath Parse - Element 2 4");
        Assertions.assertEquals("element2", xPath4.elements().get(2).name(), "XPath Parse - Element 3 4");
        XPath.Selector selector3 = xPath4.elements().get(2).selector();
        Assertions.assertTrue(selector3 instanceof XPath.ExpressionSelector, "XPath Parse - Selector Type 3");

        XPath.ExpressionSelector expressionSelector2 = (XPath.ExpressionSelector) selector3;
        XPath.Token left = expressionSelector2.left();
        Assertions.assertTrue(left instanceof XPath.MethodToken, "XPath Parse - Selector Left Type 3");
        XPath.MethodToken methodToken = (XPath.MethodToken) left;
        Assertions.assertEquals("index", methodToken.method().name(), "XPath Parse - Selector Left Value 3");
        Assertions.assertEquals(0, methodToken.method().args().size(), "XPath Parse - Selector Left Arguments 3");
        XPath.Token right = expressionSelector2.right();
        Assertions.assertTrue(right instanceof XPath.ValueToken, "XPath Parse - Selector Right Type 3");
        XPath.ValueToken valueToken = (XPath.ValueToken) right;
        Assertions.assertEquals("3", valueToken.value(), "XPath Parse - Selector Right Value 3");
        Assertions.assertSame(expressionSelector2.operator(), XPath.Operator.LESS_THAN, "XPath Parse - Selector Operator 3");
    }

    @Test
    public void testXPathSubPaths() {
        String xpath = "/root/element1/element2";
        XPath xPath = XPath.parse(xpath);
        Assertions.assertEquals("/element1/element2", xPath.subpath(1).toString(), "XPath Next 1");
        Assertions.assertEquals("/element2", xPath.subpath(2).toString(), "XPath Next 2");

        // 2 levels
        String xpath2 = "/root/element1/element2[attr1='value1']";
        XPath xPath2 = XPath.parse(xpath2);
        Assertions.assertEquals("/element2[attr1='value1']", xPath2.subpath(2).toString(), "XPath Next 3");
        // assert that level 3 is invalid (throws IllegalArgumentException)
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            xPath2.subpath(3);
        });
    }
}
