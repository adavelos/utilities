package argonath.utils.test;


import argonath.utils.reflector.reader.selector.Selector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestXPath {

    @Test
    public void testValueXPath() {
        String xpath = "/root/element1/element2";
        Selector xPath = Selector.parse(xpath);
        Assertions.assertEquals(3, xPath.elements().size(), "XPath Parse - Element Count");
        Assertions.assertEquals("root", xPath.elements().get(0).fieldName(), "XPath Parse - Element 1");
        Assertions.assertEquals("element1", xPath.elements().get(1).fieldName(), "XPath Parse - Element 2");
        Assertions.assertEquals("element2", xPath.elements().get(2).fieldName(), "XPath Parse - Element 3");

//        // Expression Selector
//        String xpath2 = "/root/element1/element2[attr1='value1']";
//        Selector xPath2 = Selector.parse(xpath2);
//        Assertions.assertEquals(3, xPath2.elements().size(), "XPath Parse - Element Count 2");
//        Assertions.assertEquals("root", xPath2.elements().get(0).fieldName(), "XPath Parse - Element 1 2");
//        Assertions.assertEquals("element1", xPath2.elements().get(1).fieldName(), "XPath Parse - Element 2 2");
//        Assertions.assertEquals("element2", xPath2.elements().get(2).fieldName(), "XPath Parse - Element 3 2");
//        Selector selector = xPath2.elements().get(2).
//        Assertions.assertTrue(selector instanceof Selector.ExpressionSelector, "XPath Parse - Selector Type");
//        Selector.ExpressionSelector expressionSelector = (Selector.ExpressionSelector) selector;
//        {
//            Selector.Token left = expressionSelector.left();
//            Assertions.assertTrue(left instanceof Selector.ValueToken, "XPath Parse - Selector Left Type");
//            Selector.ValueToken literalToken = (Selector.ValueToken) left;
//            Assertions.assertEquals("attr1", literalToken.value(), "XPath Parse - Selector Left Value");
//            Selector.Token right = expressionSelector.right();
//            Assertions.assertTrue(right instanceof Selector.ValueToken, "XPath Parse - Selector Right Type");
//            Selector.ValueToken literalToken2 = (Selector.ValueToken) right;
//            Assertions.assertEquals("value1", literalToken2.value(), "XPath Parse - Selector Right Value");
//            Assertions.assertSame(expressionSelector.operator(), Operator.EQUALS, "XPath Parse - Selector Operator");
//        }
//
//
//        // Value Selector
//        String xpath3 = "/root/element1/element2[value]";
//        Selector xPath3 = Selector.parse(xpath3);
//        Assertions.assertEquals(3, xPath3.elements().size(), "XPath Parse - Element Count 3");
//        Assertions.assertEquals("root", xPath3.elements().get(0).fieldName(), "XPath Parse - Element 1 3");
//        Assertions.assertEquals("element1", xPath3.elements().get(1).fieldName(), "XPath Parse - Element 2 3");
//        Assertions.assertEquals("element2", xPath3.elements().get(2).fieldName(), "XPath Parse - Element 3 3");
//        Selector.Selector selector2 = xPath3.elements().get(2).selector();
//        Assertions.assertTrue(selector2 instanceof Selector.ValueSelector, "XPath Parse - Selector Type 2");
//        Selector.ValueSelector valueSelector = (Selector.ValueSelector) selector2;
//        Assertions.assertEquals("value", valueSelector.value(), "XPath Parse - Selector Value");
//
//        // Expression Selector with Method Token
//        String xpath4 = "/root/element1/element2[index()<3]";
//        Selector selector4 = Selector.parse(xpath4);
//        Assertions.assertEquals(3, selector4.elements().size(), "XPath Parse - Element Count 4");
//        Assertions.assertEquals("root", selector4.elements().get(0).fieldName(), "XPath Parse - Element 1 4");
//        Assertions.assertEquals("element1", selector4.elements().get(1).fieldName(), "XPath Parse - Element 2 4");
//        Assertions.assertEquals("element2", selector4.elements().get(2).fieldName(), "XPath Parse - Element 3 4");
//        Selector.Selector selector3 = selector4.elements().get(2).selector();
//        Assertions.assertTrue(selector3 instanceof Selector.ExpressionSelector, "XPath Parse - Selector Type 3");
//
//        Selector.ExpressionSelector expressionSelector2 = (Selector.ExpressionSelector) selector3;
//        Selector.Token left = expressionSelector2.left();
//        Assertions.assertTrue(left instanceof Selector.MethodToken, "XPath Parse - Selector Left Type 3");
//        Selector.MethodToken methodToken = (Selector.MethodToken) left;
//        Assertions.assertEquals("index", methodToken.method().fieldName(), "XPath Parse - Selector Left Value 3");
//        Assertions.assertEquals(0, methodToken.method().args().size(), "XPath Parse - Selector Left Arguments 3");
//        Selector.Token right = expressionSelector2.right();
//        Assertions.assertTrue(right instanceof Selector.ValueToken, "XPath Parse - Selector Right Type 3");
//        Selector.ValueToken valueToken = (Selector.ValueToken) right;
//        Assertions.assertEquals("3", valueToken.value(), "XPath Parse - Selector Right Value 3");
//        Assertions.assertSame(expressionSelector2.operator(), Operator.LESS_THAN, "XPath Parse - Selector Operator 3");
    }


}
