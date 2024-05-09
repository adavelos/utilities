package argonath.utils.reflection;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Provides utility methods to create new objects or mutate existing objects using reflection.
 */
public class Mutator {

    public static void setFieldValue(Field field, Object obj, Object value) {
        if (value == null) {
            return; // no action
        }
        field.setAccessible(true);
        Class fieldClazz = field.getType();
        try {
            Class valueClazz = value.getClass();
            if (fieldClazz.isAssignableFrom(valueClazz)) {
                field.set(obj, value);
            }
            // if the value is String
            else if (String.class.equals(valueClazz)) {
                String valueStr = (String) value;
                if (fieldClazz.getName().equals(Integer.TYPE) || fieldClazz.equals(Integer.class)) {
                    field.set(obj, Integer.parseInt(valueStr));
                } else if (fieldClazz.getName().equals(Long.TYPE) || fieldClazz.equals(Long.class)) {
                    field.set(obj, Long.parseLong(valueStr));
                } else if (fieldClazz.equals(BigInteger.class)) {
                    field.set(obj, new BigInteger(valueStr));
                } else if (fieldClazz.equals(BigDecimal.class)) {
                    field.set(obj, new BigDecimal(valueStr));
                } else if (fieldClazz.getName().equals(Double.TYPE) || fieldClazz.equals(Double.class)) {
                    field.set(obj, Double.valueOf(valueStr));
                } else if (fieldClazz.getName().equals(Float.TYPE) || fieldClazz.equals(Float.class)) {
                    field.set(obj, Float.valueOf(valueStr));
                } else if (fieldClazz.getName().equals(Boolean.TYPE) || fieldClazz.equals(Boolean.class)) {
                    field.set(obj, Boolean.parseBoolean(valueStr));
                } else {
                    throw new RuntimeException("Cannot Assign String Value to '" + fieldClazz + "'");
                }
            }
            // else: error
            else {
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

    public static <T> T instantiate(Class<T> clazz) {
        T ret;
        if (Accessor.isList(clazz)) {
            ret = (T) new ArrayList();
        } else if (Accessor.isArray(clazz)) {
            throw new UnsupportedOperationException("Instantiation of Array is not supported");
        } else if (Accessor.isCollection(clazz)) {
            throw new UnsupportedOperationException("Instantiation of any Collection other than List is not supported");
        } else {
            ret = instantiateClassInstance(clazz);
        }
        return ret;
    }

    private static <T> T instantiateClassInstance(Class<T> clazz) {
        T ret;
        try {
            // WARNING: In theory this is very 'unsafe' however if the SPECS are compliant with the domain model, the required object will be properly instantiated
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            ret = (T) unsafe.allocateInstance(clazz);

        } catch (Exception e) {
            throw new RuntimeException("Cannot Instantiate Object of:" + clazz.getName(), e);
        }
        return ret;
    }

    public static <T> T safeInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Instantiate Error - Cannot Instantiate Class:" + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Security Error - Cannot Instantiate Class:" + clazz, e);
        }
    }

}
