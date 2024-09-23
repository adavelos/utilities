package argonath.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class that provides support for parsing / creating XPath expressions
 */
public class XPathUtil {
    private XPathUtil() {
    }
    private static final String SEP = "/";
    private static final String ROOT = "/";

    /**
     * Return an absolute X-Path expression consisting of the given elements.
     * The absolute X-Path is always prefixed with the separator character
     */
    public static String create(Collection<String> elements) {
        StringBuilder ret = new StringBuilder();
        for (String elem : elements) {
            ret.append(SEP).append(elem);
        }
        return canonicalPath(ret.toString());
    }

    /**
     * Create an X-Path expression from an array of String elements.
     */
    public static String create(String... elements) {
        if (elements == null || elements.length == 0) {
            return ROOT;
        }
        return create(Arrays.asList(elements));
    }

    /**
     * Append elements to an X-Path expression
     */
    public static String append(String xpath, Collection<String> elements) {
        Assert.notNull(xpath, "Invalid XPath: 'null'");
        StringBuilder ret = new StringBuilder(xpath);
        ret.append(create(elements));
        return canonicalPath(ret.toString());
    }

    /**
     * Append array of elements to an X-Path expression
     */
    public static String append(String xpath, String... elements) {
        return append(xpath, Arrays.asList(elements));
    }

    /**
     * Prepend elements to an X-Path expression
     */
    public static String prepend(String xpath, Collection<String> elements) {
        return append(create(elements), xpath);
    }

    /**
     * Prepend array of elements to an X-Path expression
     */
    public static String prepend(String xpath, String... elements) {
        return prepend(xpath, Arrays.asList(elements));
    }

    /**
     * Canonicalization of an X-Path expression by eliminating multiple separator sequences and removing trailing separators
     */
    public static String canonicalPath(String xpath) {
        Assert.notNull(xpath, "X-Path is 'null'");
        String regexp = SEP + "{2,}"; // make flexible to modify separator
        String ret = xpath.replaceAll(regexp, SEP); // remove multiple separators
        if (xpath.endsWith(SEP)) { // remove trailing separator (head separator is valid and defines an absolut path)
            ret = ret.substring(0, ret.length() - 1);
        }
        return ret;
    }

    /**
     * Splits an X-Path expression into a list of elements.
     * In case of root paths, returns an empty list ("" or "/")
     */
    public static List<String> split(String xpath) {
        Assert.notNull(xpath, "X-Path is 'null'");
        String[] splitElements = canonicalPath(xpath).split(SEP);
        return Arrays.stream(splitElements)
                .filter(StringUtils::isNotEmpty)
                .toList();

    }

    /**
     * Validates that an X-Path contains valid elements.
     * A valid element is an element that can be assigned to a Java variable.
     * The rule emanates from the main usage of the 'XPath' utility class: a representation of the Java Class/Object Graph.
     * <p>
     * A non-canonical X-Path is considered a valid X-Path (as it is passed through canonicalization in all utility methods
     * of this API)
     */
    public static boolean isValid(String xpath) {
        return split(xpath).stream()
                .allMatch(XPathUtil::isValidElement);
    }

    private static boolean isValidElement(String element) {
        Pair<String, String> variablePart = ExpressionUtil.parseBrackets(element);
        if (variablePart == null) {
            return false;
        }
        String elementName = variablePart.getLeft();

        // parse the "." and the left part is the variable and the second part must contain alphanumeric characters
        String[] parts = elementName.split("\\.");
        String variableName = elementName;
        if (parts.length > 1) {
            variableName = parts[0];
        }

        boolean validVariable = LanguageRules.validVariableName(variableName);
        String expression = variablePart.getRight();
        if (expression != null) {
            validVariable = validVariable && StringUtils.isNotEmpty(expression);
        }
        return validVariable;
    }

    /**
     * Strips surrounding quotes from a string (only symmetrically applied)
     */
    public static String stripQuotes(String str) {
        if (str == null || str.trim().isEmpty()) {
            return str;
        }
        // if starts with single or double quotes
        boolean startsWithQuotes = str.startsWith("\"") || str.startsWith("'");
        // if ends with single or double quotes
        boolean endsWithQuotes = str.endsWith("\"") || str.endsWith("'");

        String ret = str;
        if (startsWithQuotes && endsWithQuotes) {
            ret = str.substring(1, str.length() - 1);
        }
        return ret;
    }

    public static String combine(List<String> elements) {
        String merged = elements.stream().collect(Collectors.joining(SEP, "/", ""));
        return canonicalPath(merged);
    }

    public static String parent(String path) {
        List<String> elements = split(path);
        if (elements.size() == 1) {
            return ROOT;
        }
        if (elements.isEmpty()) {
            return null;
        }
        return create(elements.subList(0, elements.size() - 1));
    }
}
