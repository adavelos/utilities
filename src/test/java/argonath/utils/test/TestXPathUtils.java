package argonath.utils.test;

import argonath.utils.xpath.XPathUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestXPathUtils {

    @Test
    public void testXPathCreate() {
        // Varargs
        Assertions.assertEquals("/test", XPathUtil.create("test"), "XPath Build - Single Vararg");
        Assertions.assertEquals("/test/second", XPathUtil.create("test", "second"), "XPath Build - Double Varargs");
        Assertions.assertEquals("/", XPathUtil.create(), "XPath Build - Empty Arg");
        String[] arr1 = {"test", "second"};
        Assertions.assertEquals("/test/second", XPathUtil.create(arr1), "XPath Build - Array");
        String[] arr2 = null;
        Assertions.assertEquals("/", XPathUtil.create(arr2), "XPath Build - Null Array");

        // Collections
        Collection<String> col = Arrays.asList("test", "second");
        Assertions.assertEquals("/test/second", XPathUtil.create(col), "XPath Build - List");
        Set<String> col2 = new TreeSet<>(Arrays.asList("test", "second"));
        Assertions.assertEquals("/second/test", XPathUtil.create(col2), "XPath Build - Set");
    }

    @Test
    public void testXPathAppend() {
        Assertions.assertEquals("/head/second/third", XPathUtil.append("/head", "second", "third"), "XPath Build - Append Varargs");
        Assertions.assertEquals("/head/second/third/fourth", XPathUtil.append("/head", "/second/third", "fourth"), "XPath Build - Append Varargs Combo");
        Assertions.assertEquals("/head/second", XPathUtil.append("/head", "second"), "XPath Build - Append Single Vararg");
        Assertions.assertEquals("/head/second", XPathUtil.append("/head", "second", "/"), "XPath Build - Append Non Canonical Vararg");
        Assertions.assertEquals("/head/second", XPathUtil.append("/head", "/", "/second"), "XPath Build - Append Non Canonical Vararg 2");
        Assertions.assertEquals("/head/second", XPathUtil.append("/head/second", "/", "/"), "XPath Build - Append Non Canonical Varargs");
        Assertions.assertEquals("/head/second/third/fourth", XPathUtil.append("/head/second", Arrays.asList("/third", "fourth")), "XPath Build - Append Collection");
    }

    @Test
    public void testXPathPrepend() {
        Assertions.assertEquals("/second/third/tail", XPathUtil.prepend("/tail", "second", "third"), "XPath Build - Prepend Varargs");
        Assertions.assertEquals("/second/third/fourth/tail", XPathUtil.prepend("/tail", "/second/third", "fourth"), "XPath Build - Prepend Varargs Combo");
        Assertions.assertEquals("/second/tail", XPathUtil.prepend("/tail", "second"), "XPath Build - Prepend Single Vararg");
        Assertions.assertEquals("/second/tail", XPathUtil.prepend("/tail", "second", "/"), "XPath Build - Prepend Non Canonical Vararg");
        Assertions.assertEquals("/second/tail", XPathUtil.prepend("/tail", "/", "/second"), "XPath Build - Prepend Non Canonical Vararg 2");
        Assertions.assertEquals("/tail/last", XPathUtil.prepend("/tail/last", "/", "/"), "XPath Build - Prepend Non Canonical Varargs");
        Assertions.assertEquals("/first/second/tail/last", XPathUtil.prepend("/tail/last", Arrays.asList("/first", "second")), "XPath Build - Prepend Collection");
    }

    @Test
    public void testCanonicalPath() {
        Assertions.assertEquals("/test/second", XPathUtil.canonicalPath("/test/second"), "Canonical Path - No change");
        Assertions.assertEquals("test/second", XPathUtil.canonicalPath("test//second"), "Canonical Path - Relative Path");
        Assertions.assertEquals("/test/second", XPathUtil.canonicalPath("//test/second"), "Canonical Path - Invalid Head");
        Assertions.assertEquals("/test/second", XPathUtil.canonicalPath("/test////second"), "Canonical Path - Multiple Slashes");
        Assertions.assertEquals("/test/second", XPathUtil.canonicalPath("/test/second////"), "Canonical Path - Trailing Slashes");
        Assertions.assertEquals("/test/second/third/fourth", XPathUtil.canonicalPath("/test/second///third/////fourth///"), "Canonical Path - Multi Paths");
    }

    @Test
    public void testSplit() {
        Assertions.assertEquals(XPathUtil.split("").size(), 0, "Split Empty String");
        Assertions.assertEquals(XPathUtil.split("/").size(), 0, "Split Root Path");
        Assertions.assertEquals(XPathUtil.split("//").size(), 0, "Split Root Path - double slashes");
        Assertions.assertEquals("test", XPathUtil.split("/test").get(0), "Split Root Path - Single");
        Assertions.assertEquals("test", XPathUtil.split("test").get(0), "Split Root Path - Single - Relative");
        List<String> split1 = XPathUtil.split("/test/second");
        Assertions.assertEquals("test", split1.get(0), "Split Root Path - first");
        Assertions.assertEquals("second", split1.get(1), "Split Root Path - second");
    }

    @Test
    public void testIsValid() {
        Assertions.assertTrue(XPathUtil.isValid("/test/element"), "Simple Valid Path");
        Assertions.assertTrue(XPathUtil.isValid("/test//element"), "NonCanonical Valid Path");
        Assertions.assertTrue(XPathUtil.isValid("test//element"), "NonCanonical Relative Valid Path");
        Assertions.assertTrue(XPathUtil.isValid("_valid"), "Valid Element - underscore");
        Assertions.assertTrue(XPathUtil.isValid("$valid"), "Valid Element - dollar");
        Assertions.assertTrue(XPathUtil.isValid("test_valid"), "Invalid Element - underscore mid");
        Assertions.assertTrue(XPathUtil.isValid("test$valid"), "Invalid Element - dollar mid");
        Assertions.assertFalse(XPathUtil.isValid("/test/1invalid"), "Invalid Element - start with number");
        Assertions.assertFalse(XPathUtil.isValid("/_/invalid"), "Invalid Element - single underscore");
        Assertions.assertFalse(XPathUtil.isValid("//invalid element"), "Invalid Element - space");
        Assertions.assertFalse(XPathUtil.isValid("//invalid\telement"), "Invalid Element - tab");
        Assertions.assertFalse(XPathUtil.isValid("//invalid#element"), "Invalid Element - special char");

        // Brackets
        Assertions.assertTrue(XPathUtil.isValid("/test/element[1]/test"), "Brackets");
        Assertions.assertTrue(XPathUtil.isValid("/test/element[1]/test[val]"), "Multi Element Brackets");
        Assertions.assertTrue(XPathUtil.isValid("test[str]"), "Single Element Brackets");
        Assertions.assertFalse(XPathUtil.isValid("test[]"), "Empty Brackets");
        Assertions.assertFalse(XPathUtil.isValid("test[xczx]test"), "Invalid Brackets");
    }

    @Test
    public void testStripQuotes() {
        Assertions.assertEquals("test", XPathUtil.stripQuotes("\"test\""), "Strip Quotes - Double");
        Assertions.assertEquals("test", XPathUtil.stripQuotes("'test'"), "Strip Quotes - Single");
        Assertions.assertEquals("test", XPathUtil.stripQuotes("test"), "Strip Quotes - None");
        Assertions.assertEquals("\"test", XPathUtil.stripQuotes("\"test"), "Strip Quotes - Mismatch");
        Assertions.assertEquals("test\"", XPathUtil.stripQuotes("test\""), "Strip Quotes - Mismatch 2");
        Assertions.assertEquals("", XPathUtil.stripQuotes(""), "Strip Quotes - Empty");
        Assertions.assertEquals(" ", XPathUtil.stripQuotes(" "), "Strip Quotes - Single Space");
        Assertions.assertNull(XPathUtil.stripQuotes(null), "Strip Quotes - Null");
    }

    @Test
    public void testCombine() {
        Assertions.assertEquals("/test/element", XPathUtil.combine(List.of("test", "element")), "Combine - Simple");
        Assertions.assertEquals("/test/element/path", XPathUtil.combine(List.of("test", "element", "path")), "Combine - Multi");
        // test with trailing slash
        Assertions.assertEquals("/test/element/path", XPathUtil.combine(List.of("test", "element", "path/")), "Combine - Multi with trailing slash");
        // test with leading slash
        Assertions.assertEquals("/test/element/path", XPathUtil.combine(List.of("/test", "element", "path")), "Combine - Multi with leading slash");

    }
}

