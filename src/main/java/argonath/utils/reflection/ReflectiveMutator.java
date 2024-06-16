package argonath.utils.reflection;

import argonath.utils.Assert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides utility methods to create new objects or mutate existing objects using reflection.
 */
public class ReflectiveMutator {

    public static void setFieldValue(Field field, Object obj, Object value) {
        Assert.notNull(field, "Field cannot be null");
        Assert.notNull(obj, "Object cannot be null");
        if (value == null) {
            return; // no action
        }
        field.setAccessible(true);
        Class fieldClazz = field.getType();
        try {
            Class valueClazz = value.getClass();

            // direct assignment when the value is of the same type as the field
            if (fieldClazz.isAssignableFrom(valueClazz)) {
                field.set(obj, value);
            }
            // if the value is String (generated randomly from random generators based on field specs)
            else if (String.class.equals(valueClazz)) {
                /*
                    Supported types are (String is already handled above as in case of String value it is directly assignable to field):
                     - Integer
                     - Long
                     - Float
                     - Double
                     - Boolean
                     - BigDecimal
                     - BigInteger
                 */

                String valueStr = (String) value;
                if (fieldClazz.equals(Integer.class)) {
                    field.set(obj, Integer.parseInt(valueStr));
                } else if (fieldClazz.equals(Long.class)) {
                    field.set(obj, Long.parseLong(valueStr));
                } else if (fieldClazz.equals(Double.class)) {
                    field.set(obj, Double.valueOf(valueStr));
                } else if (fieldClazz.equals(Float.class)) {
                    field.set(obj, Float.valueOf(valueStr));
                } else if (fieldClazz.equals(Boolean.class)) {
                    field.set(obj, Boolean.parseBoolean(valueStr));
                } else if (fieldClazz.equals(BigInteger.class)) {
                    field.set(obj, new BigInteger(valueStr));
                } else if (fieldClazz.equals(BigDecimal.class)) {
                    field.set(obj, new BigDecimal(valueStr));
                } else {
                    throw new RuntimeException("Cannot Assign String Value to '" + fieldClazz + "'");
                }
            } else {
                try {
                    // try brute force before failing
                    field.set(obj, value);
                } catch (Exception e) {
                    throw new RuntimeException("Cannot Assign Type: '" + valueClazz + "' to '" + fieldClazz + "'");
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot Set Value for Field:" + field.getType());
        }
    }

    public static <T> T safeCast(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        if (ReflectiveAccessor.isCollection(clazz) && !ReflectiveAccessor.isCollection(object)) {
            // it is possible that during the extraction / flattening process the collection has been unwrapped
            T ret = ReflectiveFactory.instantiateCollection(clazz);
            ((Collection) ret).add(object);
            return ret;
        } else if (clazz.isAssignableFrom(object.getClass())) {
            return clazz.cast(object);
        } else {
            throw new IllegalArgumentException("Cannot cast " + object.getClass() + " to " + clazz);
        }
    }

    // return safeCast that accepts List<Object> and returns List<T>
    public static <T> List<T> safeCast(Collection<Object> list, Class<T> clazz) {
        if (list == null) {
            return null;
        }
        return list.stream()
                .map(obj -> safeCast(obj, clazz))
                .collect(Collectors.toList());
    }

}
