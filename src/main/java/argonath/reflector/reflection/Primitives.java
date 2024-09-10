package argonath.reflector.reflection;

import java.lang.reflect.Array;

public class Primitives {

    public static Class<?> getWrapperType(Class<?> primitiveType) {
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == double.class) return Double.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == short.class) return Short.class;
        return null;
    }

    public static Object convertToPrimitiveArray(Object wrapperArray, Class<?> componentType) {
        if (!componentType.isPrimitive()) {
            throw new IllegalArgumentException("Component type must be primitive");
        }

        int length = Array.getLength(wrapperArray);
        Object primitiveArray = Array.newInstance(componentType, length);

        for (int i = 0; i < length; i++) {
            Object element = Array.get(wrapperArray, i);
            if (element == null) {
                throw new NullPointerException("Cannot convert null to primitive type");
            }

            if (componentType == int.class) {
                Array.setInt(primitiveArray, i, ((Number) element).intValue());
            } else if (componentType == long.class) {
                Array.setLong(primitiveArray, i, ((Number) element).longValue());
            } else if (componentType == double.class) {
                Array.setDouble(primitiveArray, i, ((Number) element).doubleValue());
            } else if (componentType == float.class) {
                Array.setFloat(primitiveArray, i, ((Number) element).floatValue());
            } else if (componentType == short.class) {
                Array.setShort(primitiveArray, i, ((Number) element).shortValue());
            } else if (componentType == byte.class) {
                Array.setByte(primitiveArray, i, ((Number) element).byteValue());
            } else if (componentType == boolean.class) {
                Array.setBoolean(primitiveArray, i, (Boolean) element);
            } else if (componentType == char.class) {
                Array.setChar(primitiveArray, i, (Character) element);
            }
        }

        return primitiveArray;
    }

}
