package argonath.utils;

import org.apache.commons.lang3.tuple.Pair;

public class ExpressionUtil {


    public static Pair<String, String> parseBrackets(String element) {
        int start = element.indexOf('[');
        int end = element.lastIndexOf(']');
        if (start == -1 || end == -1) {
            return Pair.of(element, null);
        }
        String name = element.substring(0, start);
        String selector = element.substring(start + 1, end);
        return Pair.of(name, selector);
    }



}
