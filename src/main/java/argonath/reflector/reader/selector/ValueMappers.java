package argonath.reflector.reader.selector;

import java.util.HashMap;
import java.util.Map;

public class ValueMappers {
    private ValueMappers() {
    }
    public static final ValueMapper IDENTITY = input -> input;

    private static final Map<String, ValueMapper> valueMappersRegistry = new HashMap<>();

    static void register(String key, ValueMapper valueMapper) {
        valueMappersRegistry.put(key, valueMapper);
    }

    public static ValueMapper get(String key) {
        return valueMappersRegistry.get(key);
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
