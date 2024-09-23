package argonath.reflector.generator.codelist;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the global code list data, loaded when explicitly requested (no automatic loading occurs).
 * Each code list structure is loaded statically and is accessible to globally to all unit tests executed in the same JVM.
 * If a code list with the same name is loaded multiple times, the last loaded version will be used.
 * <p>
 * All code lists loaded from a file are String generators (conversion to simple types is automatically done within the generator).
 */
public class GlobalCodeLists {
    private GlobalCodeLists() {
    }

    private static final Map<String, CodeList<?>> codeListData = new HashMap<>();

    public static void load(String codeListFileName) {
        Map<String, CodeList<String>> loadedCodeLists = CodeLists.load(codeListFileName);
        codeListData.putAll(loadedCodeLists);
    }

    @SuppressWarnings("unchecked")
    public static <T> CodeList<T> get(String key) {
        return (CodeList<T>) codeListData.get(key);
    }

    public static void clear() {
        codeListData.clear();
    }

    @SuppressWarnings("java:S1452")
    public static Map<String, CodeList<?>> all() {
        return codeListData;
    }
}
