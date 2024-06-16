package argonath.utils.reflector.reader.types;

import argonath.utils.Assert;
import argonath.utils.reflection.ObjectFactoryStrategy;

import java.util.*;

/**
 * Registry of iterable Types with extension support
 **/
public class IterableTypes {

    private static Map<Class<?>, IterableType> ITERABLE_TYPES;

    static {
        ITERABLE_TYPES = new HashMap<>();
        ITERABLE_TYPES.put(List.class, BuiltinIterableTypes.LIST);
        ITERABLE_TYPES.put(Set.class, BuiltinIterableTypes.SET);
        ITERABLE_TYPES.put(Map.class, BuiltinIterableTypes.MAP);

        // TODO: Sort the types by class hierarchy, so that the most specific types are checked first

    }

    // register iterable type
    public static void registerIterableType(Class<?> clazz, IterableType iterableType) {
        ITERABLE_TYPES.put(clazz, iterableType);
    }

    public static IterableType iterableType(Object object) {
        return iterableType(object.getClass());
    }

    public static IterableType iterableType(Class<?> clazz) {
        for (Class<?> iterableType : ITERABLE_TYPES.keySet()) {
            if (iterableType.isAssignableFrom(clazz)) {
                return ITERABLE_TYPES.get(iterableType);
            }
        }
        return null;
    }

    public static boolean isIterableType(Class<?> clazz) {
        for (Class<?> iterableType : ITERABLE_TYPES.keySet()) {
            if (iterableType.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIterableType(Object object) {
        return isIterableType(object.getClass());
    }

    public static Collection<Object> asCollection(Object object, ObjectFactoryStrategy strategy) {
        Assert.notNull(object, "Object cannot be null");

        Class<?> clazz = object.getClass();
        IterableType iterableType = iterableType(clazz);
        if (iterableType == null) {
            throw new IllegalArgumentException("Class " + clazz + " is not an iterable type");
        }
        return (Collection<Object>) iterableType.asCollection(object, strategy);
    }
}
