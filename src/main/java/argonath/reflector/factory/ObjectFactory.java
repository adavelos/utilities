package argonath.reflector.factory;

import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.registry.TypeRegistry;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;

/**
 * Provides methods to instantiate objects and arrays of objects, using a Type Registry with default value suppliers.
 */
public class ObjectFactory {
    private ObjectFactory() {
    }

    // Registry for default values of various types (i.e. collections)
    private static final TypeRegistry<Supplier<?>> DEFAULT_VALUES = new TypeRegistry<>(false);

    public static <T> void register(Class<T> type, Supplier<T> generator) {
        DEFAULT_VALUES.register(type, generator);
    }

    static {
        registerBuiltInTypes();
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> type) {
        Supplier<?> supplier = DEFAULT_VALUES.match(type);
        if (supplier != null) {
            return (T) supplier.get();
        }
        return ReflectiveMutator.instantiate(type);
    }

    public static Object createArray(Class<?> clazz) {
        if (!ReflectiveAccessor.isArray(clazz)) {
            throw new IllegalArgumentException("Class is not an array: " + clazz.getName());
        }
        // determine the component type
        Class<?> componentType = ReflectiveAccessor.getArrayType(clazz);
        // instantiate array of componentType
        return Array.newInstance(componentType, 0);
    }

    private static void registerBuiltInTypes() {
        // Collections
        register(java.util.Collection.class, ArrayList::new);
        register(java.util.List.class, ArrayList::new);
        register(java.util.LinkedList.class, LinkedList::new);
        register(java.util.Set.class, HashSet::new);
        register(java.util.SortedSet.class, TreeSet::new);
        register(java.util.Map.class, HashMap::new);
        register(java.util.SortedMap.class, TreeMap::new);
    }

}
