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
public class Accessor {
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

    public static Object getFieldValue(Object obj, String fieldName, Class clazz) {
        if (clazz == null) {
            return null;
        }
        Field f = getField(fieldName, clazz);
        if (f == null) {
            return null;
        }
        f.setAccessible(true);
        Object value = null;
        try {
            value = f.get(obj);
        } catch (IllegalAccessException e) {
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

    public static Class getGenericType(Field field) {
        Assert.notNull(field, "Field is 'null'");

        Type genericType = field.getGenericType();
        Class ret = null;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericType;
            ret = (Class) paramType.getActualTypeArguments()[0];
        }
        return ret;
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
