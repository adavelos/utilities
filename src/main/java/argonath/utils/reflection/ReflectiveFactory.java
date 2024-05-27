package argonath.utils.reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.*;

import static argonath.utils.reflection.ObjectFactoryStrategy.DEFAULT_STRATEGY;

@SuppressWarnings("unchecked")
public class ReflectiveFactory {

    /**
     * Instantiate an object of the given class type.
     * Some specific types are handled separately:
     * - Collection
     * - Map
     * - Array
     * Classes are instantiated using 'Unsafe'
     */
    public static <T> T instantiate(Class<T> clazz) {
        T ret;
        if (ReflectiveAccessor.isCollection(clazz)) {
            ret = (T) instantiateCollection(clazz);
        } else if (ReflectiveAccessor.isMap(clazz)) {
            ret = instantiateMap(clazz);
        } else if (ReflectiveAccessor.isArray(clazz)) {
            ret = instantiateArray(clazz);
        } else {
            ret = instantiateClassInstance(clazz);
        }
        return ret;
    }

    /**
     * Instantiate a Collection of the given class type.
     * Supports Lists and Sets
     */
    public static <T> T instantiateCollection(Class<T> clazz) {
        return instantiateCollection(clazz, null);
    }

    public static <T> T instantiateCollection(Class<T> clazz, Class<?> defaultType) {
        if (ReflectiveAccessor.isList(clazz)) {
            return instantiateList(clazz, defaultType);
        } else if (ReflectiveAccessor.isSet(clazz)) {
            return instantiateSet(clazz, defaultType);
        } else {
            throw new UnsupportedOperationException("Instantiation of any Collection other than List or Set is not supported");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T instantiateList(Class<T> clazz) {
        return instantiateList(clazz, null);
    }

    public static <T> T instantiateList(Class<T> clazz, Class<?> defaultType) {
        if (ReflectiveAccessor.isList(clazz)) {
            if (ArrayList.class.isAssignableFrom(clazz)) {
                return (T) new ArrayList<>();
            } else if (LinkedList.class.isAssignableFrom(clazz)) {
                return (T) new LinkedList<>();
            } else if (defaultType != null) {
                return (T) instantiate(defaultType);
            } else {
                return (T) instantiateList(DEFAULT_STRATEGY.defaultListClass(), null);
            }
        } else {
            throw new IllegalArgumentException("Class is not a List: " + clazz);
        }
    }

    /**
     * Instantiate a Set of the given class type.
     * The order of types is from more specific to more generic:
     * - LinkedHashSet
     * - HashSet
     * - TreeSet
     * - Set
     * Default type for Set is HashSet
     */
    @SuppressWarnings("unchecked")
    public static <T> T instantiateSet(Class<T> clazz, Class<?> defaultType) {
        if (defaultType != null && !Set.class.isAssignableFrom(defaultType)) {
            throw new IllegalArgumentException("Default Type is not a Set: " + defaultType);
        }

        if (ReflectiveAccessor.isSet(clazz)) {
            if (LinkedHashSet.class.isAssignableFrom(clazz)) {
                return (T) new LinkedHashSet<>();
            } else if (HashSet.class.isAssignableFrom(clazz)) {
                return (T) new HashSet<>();
            } else if (TreeSet.class.isAssignableFrom(clazz)) {
                return (T) new TreeSet<>();
            } else if (defaultType != null) {
                return (T) instantiate(defaultType);
            } else {
                return (T) instantiateSet(DEFAULT_STRATEGY.defaultSetClass(), null);
            }
        } else {
            throw new IllegalArgumentException("Unsupported Set Type: " + clazz);
        }
    }

    /**
     * Instantiate a Set of the given class type.
     * Default type for Set is HashSet
     */
    public static <T> T instantiateSet(Class<T> clazz) {
        return instantiateSet(clazz, null);
    }

    /**
     * Instantiate a Map of the given class type.
     * Default type for Map is HashMap
     */
    public static <T> T instantiateMap(Class<T> clazz) {
        return instantiateMap(clazz, null);
    }

    /**
     * Instantiate a Map of the given class type.
     * The order of types is from more specific to more generic:
     * - LinkedHashMap
     * - HashMap
     * - TreeMap
     * - Map
     * Default type for Map is HashMap
     */
    @SuppressWarnings("unchecked")
    public static <T> T instantiateMap(Class<T> clazz, Class<?> defaultType) {
        if (defaultType != null && !Map.class.isAssignableFrom(defaultType)) {
            throw new IllegalArgumentException("Default Type is not a Map: " + defaultType);
        }

        if (ReflectiveAccessor.isMap(clazz)) {
            if (LinkedHashMap.class.isAssignableFrom(clazz)) {
                return (T) new LinkedHashMap<>();
            } else if (HashMap.class.isAssignableFrom(clazz)) {
                return (T) new HashMap<>();
            } else if (TreeMap.class.isAssignableFrom(clazz)) {
                return (T) new TreeMap<>();
            } else if (defaultType != null) {
                return (T) instantiate(defaultType);
            } else {
                return (T) instantiateMap(DEFAULT_STRATEGY.defaultMapClass(), null);
            }
        } else {
            throw new IllegalArgumentException("Unsupported Map Type: " + clazz);
        }

    }

    /**
     * Instantiate an array of size 'size'
     */
    @SuppressWarnings("unchecked")
    public static <T> T instantiateArray(Class<T> clazz, int size) {
        if (!ReflectiveAccessor.isArray(clazz)) {
            throw new IllegalArgumentException("Unsupported Array Type: " + clazz);
        }

        return (T) new Object[size];
    }

    /**
     * Instantiate an empty array
     */
    public static <T> T instantiateArray(Class<T> clazz) {
        return instantiateArray(clazz, 0);
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiateClassInstance(Class<T> clazz) {
        T ret;
        try {
            // WARNING: In theory this is very 'unsafe' and assumes the reflective instantiation of fields is safe
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            ret = (T) unsafe.allocateInstance(clazz);

        } catch (Exception e) {
            throw new RuntimeException("Cannot Instantiate Object of:" + clazz.getName(), e);
        }
        return ret;
    }

}
