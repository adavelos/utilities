package argonath.utils;

import java.util.regex.Pattern;

public class LanguageRules {
    private LanguageRules() {
    }

    static final Pattern JAVA_VARIABLE_PATTERN = Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$]*");


    public static boolean validVariableName(String variableName) {
        if ("_".equals(variableName)) {
            return false; // the regexp does not match single underscore
        }
        return JAVA_VARIABLE_PATTERN.matcher(variableName).matches();
    }

}
