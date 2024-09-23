package argonath.reflector.reflection;

import argonath.reflector.types.builtin.Collections;
import argonath.utils.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides utility methods to access object / class information using Reflection
 */
public class ReflectiveAccessor {
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> ret = Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers())).collect(Collectors.toList());
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            ret.addAll(getFields(superClass));
        }
        return ret;
    }

    public static Field getField(String fieldName, Class<?> clazz) {
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
            // no action
        }
        return value;
    }

    public static boolean isStandardMultiValueClass(Class<?> clazz) {
        return isCollection(clazz) || isArray(clazz) || isMap(clazz);
    }

    public static boolean isStandardMultiValueClass(Object obj) {
        return isCollection(obj) || isArray(obj) || isMap(obj);
    }

    public static boolean isArrayOrCollection(Object obj) {
        Assert.notNull(obj, "Cannot determine Array/Collection from 'null' Object");
        return isArrayOrCollection(obj.getClass());
    }

    public static boolean isArrayOrCollection(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Array/Collection from 'null' Class");
        return isCollection(clazz) || isArray(clazz);
    }

    public static boolean isArray(Object obj) {
        Assert.notNull(obj, "Cannot determine Array from 'null' Object");
        return isArray(obj.getClass());
    }

    // isIterableArray (array not byte[])
    public static boolean isIterableArray(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Array from 'null' Class");
        return clazz.isArray() && !isByteArray(clazz);
    }

    public static Class<?> getArrayType(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Array from 'null' Class");
        if (!isArray(clazz)) {
            throw new IllegalArgumentException("Class is not an array: " + clazz.getName());
        }
        return clazz.getComponentType();
    }

    public static boolean isArray(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Array from 'null' Class");
        return clazz.isArray();
    }

    public static boolean isArrayOfPrimitives(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (!isArray(clazz)) {
            return false;
        }
        Class<?> componentType = getArrayType(clazz);
        return componentType.isPrimitive();
    }

    public static boolean isCollection(Object obj) {
        Assert.notNull(obj, "Cannot determine Collection from 'null' Object");
        return isCollection(obj.getClass());
    }

    public static boolean isCollection(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Collection from 'null' Class");
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isList(Object obj) {
        Assert.notNull(obj, "Cannot determine List from 'null' Object");
        return isList(obj.getClass());
    }

    public static boolean isList(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine List from 'null' Class");
        return List.class.isAssignableFrom(clazz);
    }

    public static boolean isMap(Object obj) {
        Assert.notNull(obj, "Cannot determine Map from 'null' Object");
        return isMap(obj.getClass());
    }

    public static boolean isMap(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Map from 'null' Class");
        return Map.class.isAssignableFrom(clazz);
    }

    public static boolean isSet(Object obj) {
        Assert.notNull(obj, "Cannot determine Set from 'null' Object");
        return Set.class.isAssignableFrom(obj.getClass());
    }

    public static boolean isSet(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine Set from 'null' Class");
        return Set.class.isAssignableFrom(clazz);
    }

    public static <T> boolean isEnum(Class<T> clazz) {
        return clazz.isEnum();
    }

    public static Class<?> getGenericType(Field field) {
        Assert.notNull(field, "Field is 'null'");
        Type genericType = field.getGenericType();
        return getClassFromCollection(genericType);
    }

    public static Class<?> getClassFromCollection(Type type) {
        Assert.notNull(type, "Type is 'null'");
        Class<?> ret = null;
        if (type instanceof ParameterizedType paramType) {
            Type actualType = paramType.getActualTypeArguments()[0];
            if (actualType instanceof Class<?> clazz) {
                ret = clazz;
            } else {
                ret = (Class<?>) ((ParameterizedType) actualType).getRawType();
            }
        }
        return ret;
    }

    public static Class<?> getClassFromCollection(Collection<?> collection) {
        Assert.notNull(collection, "Collection is 'null'");
        if (collection.isEmpty()) {
            return Object.class;
        }
        return collection.iterator().next().getClass();
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getClassFromCollectionTyped(Collection<T> collection) {
        return (Class<T>) getClassFromCollection(collection);
    }

    public static Class<?> getGenericOrArrayType(Field field) {
        Class<?> ret = getGenericType(field);
        if (ret == null) { // arrays
            ret = getArrayType(field.getType());
        }
        return ret;
    }

    public static List getArrayOrCollectionValues(Object obj) {
        Assert.notNull(obj, "Cannot get Array/Collection values from 'null' Object");
        Class<?> clazz = obj.getClass();
        List<Object> ret;
        if (isArray(obj)) {
            Object[] arr = (Object[]) obj;
            ret = Arrays.asList(arr);
        } else if (isCollection(obj)) {
            Collection<?> col = (Collection<?>) obj;
            ret = new ArrayList<>(col);
        } else {
            throw new IllegalArgumentException("Unsupported Class:" + clazz);
        }
        return ret;
    }

    public static <T, Y> boolean isListOfCompatibleType(List<T> list, Class<Y> clazz) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Cannot determine if List of Simple Type for 'null' List");
        }
        Class<?> actualClazz = list.get(0).getClass();
        return actualClazz.isAssignableFrom(clazz);
    }

    public static boolean isByteArray(Class<?> clazz) {
        return clazz.isArray() && clazz.getComponentType() != null && clazz.getComponentType().equals(Byte.TYPE);
    }

    public static Integer getListSize(Object object) {
        if (object == null) {
            return 0;
        }
        if (isList(object)) {
            List<?> list = Collections.asList(object);
            return list.size();
        } else {
            return 0;
        }
    }

    public static List<Class<?>> getAncestors(Class<?> type) {
        List<Class<?>> ancestors = new ArrayList<>();
        // add to the ancestors the direct superclass
        Class<?> superClass = type.getSuperclass();
        if (superClass != null) {
            ancestors.add(superClass);
        }

        // add to the ancestors the direct interfaces
        Class<?>[] interfaces = type.getInterfaces();
        ancestors.addAll(Arrays.asList(interfaces));
        return ancestors;
    }


}
