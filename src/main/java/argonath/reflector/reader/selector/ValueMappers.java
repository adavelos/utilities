package argonath.reflector.reader.selector;

import java.util.HashMap;
import java.util.Map;

public class ValueMappers {
    public static final ValueMapper IDENTITY = input -> input;

    private static final Map<String, ValueMapper> valueMappers = new HashMap<>();

    static void register(String key, ValueMapper valueMapper) {
        valueMappers.put(key, valueMapper);
    }

    public static ValueMapper get(String key) {
        return valueMappers.get(key);
    }

    static {
        registerBuiltInValueMappers();
    }

    private static void registerBuiltInValueMappers() {
        register("key", BuiltInValueMappers.MAP_KEY_VM);
        register("value", BuiltInValueMappers.MAP_VALUE_VM);
    }

    public static ValueMapper mapValueMapper() {
        return BuiltInValueMappers.MAP_VALUE_VM;
    }

    public static ValueMapper mapVKeyMapper() {
        return BuiltInValueMappers.MAP_KEY_VM;
    }
}
