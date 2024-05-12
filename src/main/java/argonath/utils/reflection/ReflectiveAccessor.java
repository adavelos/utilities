package argonath.utils.reflection;

import argonath.utils.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import static argonath.utils.reflection.Specs.SUPPORTED_TYPES;

/**
 * Provides utility methods to access object / class information using Reflection
 */
public class ReflectiveAccessor {
    public static List<Field> getFields(Class clazz) {
        List<Field> fields = Arrays.asList(clazz.getDeclaredFields()).stream()
                .filter(field -> !Modifier.isStatic(field.getModifiers())) // ignore static fields
                .collect(Collectors.toList());
        List<Field> ret = new ArrayList<>(fields);
        Class superClass = clazz.getSuperclass();
        if (superClass != null) {
            ret.addAll(getFields(superClass));
        }
        return ret;
    }

    public static Field getField(String fieldName, Class clazz) {
        Field f = null;
        try {
            f = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                f = getField(fieldName, clazz.getSuperclass());
            }
        }
        return f;
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        if (obj == null) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Field f = getField(fieldName, clazz);
        if (f == null) {
            throw new IllegalArgumentException("Field not found: " + fieldName + " in class: " + clazz.getName());
        }
        f.setAccessible(true);
        Object value = null;
        try {
            value = f.get(obj);
        } catch (IllegalAccessException ignored) {
        }
        return value;
    }

    public static Boolean isArrayOrCollection(Object obj) {
        Assert.notNull(obj, "Cannot determine Array/Collection from 'null' Object");
        return isArrayOrCollection(obj.getClass());
    }

    public static Boolean isArrayOrCollection(Class clazz) {
        Assert.notNull(clazz, "Cannot determine Array/Collection from 'null' Class");
        return isCollection(clazz) || isArray(clazz);
    }

    public static Boolean isArray(Object obj) {
        Assert.notNull(obj, "Cannot determine Array from 'null' Object");
        return isArray(obj.getClass());
    }

    public static Boolean isArray(Class clazz) {
        Assert.notNull(clazz, "Cannot determine Array from 'null' Class");
        return clazz.isArray();
    }

    public static Boolean isCollection(Object obj) {
        Assert.notNull(obj, "Cannot determine Collection from 'null' Object");
        return isCollection(obj.getClass());
    }

    public static Boolean isCollection(Class clazz) {
        Assert.notNull(clazz, "Cannot determine Collection from 'null' Class");
        return List.class.isAssignableFrom(clazz);
    }

    public static Boolean isList(Object obj) {
        Assert.notNull(obj, "Cannot determine List from 'null' Object");
        return isList(obj.getClass());
    }

    public static Boolean isList(Class clazz) {
        Assert.notNull(clazz, "Cannot determine List from 'null' Class");
        return List.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Object obj) {
        Assert.notNull(obj, "Cannot determine Map from 'null' Object");
        return isMap(obj.getClass());
    }

    public static boolean isMap(Class clazz) {
        Assert.notNull(clazz, "Cannot determine Map from 'null' Class");
        return Map.class.isAssignableFrom(clazz);
    }

    // isSet
    public static boolean isSet(Object obj) {
        Assert.notNull(obj, "Cannot determine Set from 'null' Object");
        return Set.class.isAssignableFrom(obj.getClass());
    }

    public static Class getGenericType(Field field) {
        Assert.notNull(field, "Field is 'null'");
        Type genericType = field.getGenericType();
        return getGenericType(genericType);
    }

    public static Class getGenericType(Type type) {
        Assert.notNull(type, "Type is 'null'");
        Class ret = null;
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            ret = (Class) paramType.getActualTypeArguments()[0];
        }
        return ret;
    }

    public static boolean isCollectionOfSimpleType(Object object, Field field) {
        if (!isCollection(object)) {
            return false;
        }
        Class clazz = getGenericType(field);
        return isSupportedSimpleType(clazz);
    }

    public static List<Object> getArrayOrCollectionValues(Object obj) {
        Assert.notNull(obj, "Cannot get Array/Collection values from 'null' Object");
        Class clazz = obj.getClass();
        List<Object> ret;
        if (isArray(obj)) {
            Object[] arr = (Object[]) obj;
            ret = Arrays.asList(arr);
        } else if (isCollection(obj)) {
            Collection col = (Collection) obj;
            ret = new ArrayList<>(col);
        } else {
            throw new RuntimeException("Unsupported Class:" + clazz);
        }
        return ret;
    }

    public static Boolean isCollectionOfSimpleType(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException("Cannot determine if List of Simple Type for 'null' List");
        }
        Class clazz = collection.iterator().next().getClass();
        return isSupportedSimpleType(clazz);
    }

    public static boolean isNavigableCollection(Object obj) {
        Assert.notNull(obj, "Cannot determine Navigable Collection from 'null' Object");
        return isNavigableCollection(obj.getClass());
    }

    // Navigable Types: List, Set, Map, arrays
    public static boolean isNavigableCollection(Class clazz) {
        return isList(clazz) || isMap(clazz) || isSet(clazz) || isArray(clazz);
    }

    // input: Object, output Collection / name: asCollection
    public static Collection<?> asCollection(Object obj) {
        if (obj == null) {
            return null;
        }
        if (isList(obj)) {
            return (List<?>) obj;
        }
        if (isSet(obj)) {
            return (Set<?>) obj;
        }
        if (isMap(obj)) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.entrySet();
        }
        if (isArray(obj)) {
            return Arrays.asList((Object[]) obj);
        }
        throw new IllegalArgumentException("Unsupported Navigable Type: " + obj.getClass());
    }

    public static <T, Y> boolean isListOfCompatibleType(List<T> list, Class<Y> clazz) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Cannot determine if List of Simple Type for 'null' List");
        }
        Class<?> actualClazz = list.get(0).getClass();
        return actualClazz.isAssignableFrom(clazz);
    }

    public static Boolean isSupportedSimpleType(Class clazz) {
        Assert.notNull(clazz, "Cannot resolve if Simple Type for 'null' Object");
        return clazz.isPrimitive() || clazz.isEnum() || SUPPORTED_TYPES.contains(clazz);
    }

    public static boolean isByteArray(Class clazz) {
        return clazz.isArray() && clazz.getComponentType() != null && clazz.getComponentType().equals(Byte.TYPE);
    }

    public static Integer getListSize(Object object) {
        if (object == null) {
            return 0;
        }
        if (isList(object)) {
            List list = (List) object;
            return list.size();
        } else {
            return 0;
        }
    }

}
