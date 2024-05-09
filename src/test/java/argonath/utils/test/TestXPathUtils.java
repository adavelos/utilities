package argonath.utils.test;

import argonath.utils.Assert;
import argonath.utils.xpath.XPathUtil;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestXPathUtils {

    @Test
    public void testXPathCreate() {
        // Varargs
        Assert.isTrue(XPathUtil.create("test").equals("/test"), "XPath Build - Single Vararg");
        Assert.isTrue(XPathUtil.create("test", "second").equals("/test/second"), "XPath Build - Double Varargs");
        Assert.isTrue(XPathUtil.create().equals("/"), "XPath Build - Empty Arg");
        String[] arr1 = {"test", "second"};
        Assert.isTrue(XPathUtil.create(arr1).equals("/test/second"), "XPath Build - Array");
        String[] arr2 = null;
        Assert.isTrue(XPathUtil.create(arr2).equals("/"), "XPath Build - Null Array");

        // Collections
        Collection<String> col = Arrays.asList("test", "second");
        Assert.isTrue(XPathUtil.create(col).equals("/test/second"), "XPath Build - List");
        Set<String> col2 = new TreeSet(Arrays.asList("test", "second"));
        Assert.isTrue(XPathUtil.create(col2).equals("/second/test"), "XPath Build - Set");
    }

    @Test
    public void testXPathAppend() {
        Assert.isTrue(XPathUtil.append("/head", "second", "third").equals("/head/second/third"), "XPath Build - Append Varargs");
        Assert.isTrue(XPathUtil.append("/head", "/second/third", "fourth").equals("/head/second/third/fourth"), "XPath Build - Append Varargs Combo");
        Assert.isTrue(XPathUtil.append("/head", "second").equals("/head/second"), "XPath Build - Append Single Vararg");
        Assert.isTrue(XPathUtil.append("/head", "second", "/").equals("/head/second"), "XPath Build - Append Non Canonical Vararg");
        Assert.isTrue(XPathUtil.append("/head", "/", "/second").equals("/head/second"), "XPath Build - Append Non Canonical Vararg 2");
        Assert.isTrue(XPathUtil.append("/head/second", "/", "/").equals("/head/second"), "XPath Build - Append Non Canonical Varargs");
        Assert.isTrue(XPathUtil.append("/head/second", Arrays.asList("/third", "fourth")).equals("/head/second/third/fourth"), "XPath Build - Append Collection");
    }

    @Test
    public void testXPathPrepend() {
        Assert.isTrue(XPathUtil.prepend("/tail", "second", "third").equals("/second/third/tail"), "XPath Build - Prepend Varargs");
        Assert.isTrue(XPathUtil.prepend("/tail", "/second/third", "fourth").equals("/second/third/fourth/tail"), "XPath Build - Prepend Varargs Combo");
        Assert.isTrue(XPathUtil.prepend("/tail", "second").equals("/second/tail"), "XPath Build - Prepend Single Vararg");
        Assert.isTrue(XPathUtil.prepend("/tail", "second", "/").equals("/second/tail"), "XPath Build - Prepend Non Canonical Vararg");
        Assert.isTrue(XPathUtil.prepend("/tail", "/", "/second").equals("/second/tail"), "XPath Build - Prepend Non Canonical Vararg 2");
        Assert.isTrue(XPathUtil.prepend("/tail/last", "/", "/").equals("/tail/last"), "XPath Build - Prepend Non Canonical Varargs");
        Assert.isTrue(XPathUtil.prepend("/tail/last", Arrays.asList("/first", "second")).equals("/first/second/tail/last"), "XPath Build - Prepend Collection");
    }

    @Test
    public void testCanonicalPath() {
        Assert.isTrue(XPathUtil.canonicalPath("/test/second").equals("/test/second"), "Canonical Path - No change");
        Assert.isTrue(XPathUtil.canonicalPath("test//second").equals("test/second"), "Canonical Path - Relative Path");
        Assert.isTrue(XPathUtil.canonicalPath("//test/second").equals("/test/second"), "Canonical Path - Invalid Head");
        Assert.isTrue(XPathUtil.canonicalPath("/test////second").equals("/test/second"), "Canonical Path - Multiple Slashes");
        Assert.isTrue(XPathUtil.canonicalPath("/test/second////").equals("/test/second"), "Canonical Path - Trailing Slashes");
        Assert.isTrue(XPathUtil.canonicalPath("/test/second///third/////fourth///").equals("/test/second/third/fourth"), "Canonical Path - Multi Paths");
    }

    @Test
    public void testSplit() {
        Assert.isTrue(XPathUtil.split("").size() == 0, "Split Empty String");
        Assert.isTrue(XPathUtil.split("/").size() == 0, "Split Root Path");
        Assert.isTrue(XPathUtil.split("//").size() == 0, "Split Root Path - double slashes");
        Assert.isTrue(XPathUtil.split("/test").get(0).equals("test"), "Split Root Path - Single");
        Assert.isTrue(XPathUtil.split("test").get(0).equals("test"), "Split Root Path - Single - Relative");
        List<String> split1 = XPathUtil.split("/test/second");
        Assert.isTrue(split1.get(0).equals("test"), "Split Root Path - first");
        Assert.isTrue(split1.get(1).equals("second"), "Split Root Path - second");
    }

    @Test
    public void testIsValid() {
        Assert.isTrue(XPathUtil.isValid("/test/element"), "Simple Valid Path");
        Assert.isTrue(XPathUtil.isValid("/test//element"), "NonCanonical Valid Path");
        Assert.isTrue(XPathUtil.isValid("test//element"), "NonCanonical Relative Valid Path");
        Assert.isTrue(XPathUtil.isValid("_valid"), "Valid Element - underscore");
        Assert.isTrue(XPathUtil.isValid("$valid"), "Valid Element - dollar");
        Assert.isTrue(XPathUtil.isValid("test_valid"), "Invalid Element - underscore mid");
        Assert.isTrue(XPathUtil.isValid("test$valid"), "Invalid Element - dollar mid");
        Assert.isTrue(!XPathUtil.isValid("/test/1invalid"), "Invalid Element - start with number");
        Assert.isTrue(!XPathUtil.isValid("/_/invalid"), "Invalid Element - single underscore");
        Assert.isTrue(!XPathUtil.isValid("//invalid element"), "Invalid Element - space");
        Assert.isTrue(!XPathUtil.isValid("//invalid\telement"), "Invalid Element - tab");
        Assert.isTrue(!XPathUtil.isValid("//invalid#element"), "Invalid Element - special char");

        // Brackets
        Assert.isTrue(XPathUtil.isValid("/test/element[1]/test"), "Brackets");
        Assert.isTrue(XPathUtil.isValid("/test/element[1]/test[val]"), "Multi Element Brackets");
        Assert.isTrue(XPathUtil.isValid("test[str]"), "Single Element Brackets");
        Assert.isTrue(!XPathUtil.isValid("test[]"), "Empty Brackets");
        Assert.isTrue(!XPathUtil.isValid("test[xczx]test"), "Invalid Brackets");
    }

    @Test
    public void testStripQuotes() {
        Assert.isTrue(XPathUtil.stripQuotes("\"test\"").equals("test"), "Strip Quotes - Double");
        Assert.isTrue(XPathUtil.stripQuotes("'test'").equals("test"), "Strip Quotes - Single");
        Assert.isTrue(XPathUtil.stripQuotes("test").equals("test"), "Strip Quotes - None");
        Assert.isTrue(XPathUtil.stripQuotes("\"test").equals("\"test"), "Strip Quotes - Mismatch");
        Assert.isTrue(XPathUtil.stripQuotes("test\"").equals("test\""), "Strip Quotes - Mismatch 2");
        Assert.isTrue(XPathUtil.stripQuotes("").equals(""), "Strip Quotes - Empty");
        Assert.isTrue(XPathUtil.stripQuotes(" ").equals(" "), "Strip Quotes - Single Space");
        Assert.isTrue(XPathUtil.stripQuotes(null) == null, "Strip Quotes - Null");
    }
}

