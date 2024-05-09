package argonath.utils.xpath;

import org.apache.commons.lang3.tuple.Pair;

public class ExpressionUtil {

    public static Pair<String, String> parseBrackets(String element) {
        int start = element.indexOf('[');
        int end = element.lastIndexOf(']');
        if (end > -1 && end < element.length() - 1) {
            // there is text after the closing bracket
            return null;
        }
        if (start == -1 || end == -1) {
            return Pair.of(element, null);
        }
        String name = element.substring(0, start);
        String selector = element.substring(start + 1, end);
        return Pair.of(name, selector);
    }


}
