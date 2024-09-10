package argonath.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WcMatcher {

    public static boolean match(String value, String pattern) {
        // Convert wildcard pattern to regex pattern
        String regexPattern = pattern
                .replace(".", "\\.")
                .replace("*", ".*");

        // Ensure the pattern matches the entire string
        regexPattern = "^" + regexPattern + "$";

        Pattern compiledPattern = Pattern.compile(regexPattern);
        Matcher matcher = compiledPattern.matcher(value);
        return matcher.matches();
    }

    public static boolean isWildcard(String pattern) {
        return pattern.contains("*");
    }
}
