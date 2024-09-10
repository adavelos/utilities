package argonath.reflector.types.iterable;

import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.registry.TypeRegistry;
import argonath.utils.Assert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Registry of iterable Types with extension support
 **/
public class IterableTypes {

    private static TypeRegistry<IterableType> registry = new TypeRegistry<>(true);

    static {
        registerBuiltInTypes();
    }


    public static void register(Class<?> type, IterableType iterableType) {
        registry.register(type, iterableType);
    }

    public static void registerBuiltInTypes() {
        register(Collection.class, BuiltInIterableTypes.COLLECTION);
        register(List.class, BuiltInIterableTypes.LIST);
        register(List.class, BuiltInIterableTypes.LINKED_LIST);
        register(Set.class, BuiltInIterableTypes.SET);
        register(Map.class, BuiltInIterableTypes.MAP);
        register(Collection.class, BuiltInIterableTypes.ARRAY);
    }

    public static IterableType iterableType(Object object) {
        return iterableType(object.getClass());
    }

    public static IterableType iterableType(Class<?> clazz) {
        if (ReflectiveAccessor.isIterableArray(clazz)) {
            return BuiltInIterableTypes.ARRAY;
        }
        return registry.match(clazz);
    }

    public static boolean isIterableType(Class<?> clazz) {
        if (ReflectiveAccessor.isIterableArray(clazz)) {
            return true;
        }
        return iterableType(clazz) != null;
    }

    public static boolean isIterableType(Object object) {
        return isIterableType(object.getClass());
    }

    public static Collection<Object> asCollection(Object object, Class<?> classType) {
        Assert.notNull(object, "Object cannot be null");

        IterableType iterableType = iterableType(classType);
        if (iterableType == null) {
            throw new IllegalArgumentException("Class: " + classType + " is not an iterable type");
        }
        return (Collection<Object>) iterableType.asCollection(object);
    }

}
