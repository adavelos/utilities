package argonath.reflector.reflection;

import argonath.reflector.factory.ObjectFactory;
import argonath.reflector.types.iterable.IterableTypes;
import argonath.reflector.types.simple.SimpleType;
import argonath.reflector.types.simple.SimpleTypes;
import argonath.utils.Assert;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Provides utility methods to create new objects or mutate existing objects using reflection.
 */
public class ReflectiveMutator {
    private ReflectiveMutator() {
    }

    public static void setFieldValue(Field field, Object obj, Object value) {
        Assert.notNull(field, "Field cannot be null");
        Assert.notNull(obj, "Object cannot be null");
        if (value == null) {
            return; // no action
        }
        field.setAccessible(true);
        Class<?> fieldClazz = field.getType();
        try {
            Class<?> valueClazz = value.getClass();

            // Special handling for primitive arrays: cannot assign an array of primitive wrappers to a primitive array
            if (ReflectiveAccessor.isArrayOfPrimitives(fieldClazz) && value.getClass().isArray()) {
                value = Primitives.convertToPrimitiveArray(value, fieldClazz.getComponentType());
            }

            // direct assignment when the value is of the same type as the field
            if (fieldClazz.isAssignableFrom(valueClazz)) {
                field.set(obj, value);
            } else if (value instanceof String valueStr && SimpleTypes.isSimpleType(fieldClazz)) {
                // Simple Types convertible from String
                SimpleType<?> simpleType = SimpleTypes.simpleType(fieldClazz);
                field.set(obj, simpleType.fromString(valueStr));
            // TODO: here an additional 'else' will be added for custom type converters
            } else {
                try {
                    // try brute force before failing
                    field.set(obj, value);
                } catch (Exception e) {
                    throw new IllegalStateException("Cannot Assign Type: '" + valueClazz + "' to '" + fieldClazz + "'");
                }
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot Set Value for Field:" + field.getType());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T safeCast(Object object, Class<T> clazz) {
        if (object == null) {
            return null;
        }
        if (ReflectiveAccessor.isCollection(clazz) && !ReflectiveAccessor.isCollection(object)) {
            // it is possible that during the resolution / flattening process the collection has been unwrapped
            T ret = ObjectFactory.create(clazz);
            ((Collection<Object>) ret).add(object);
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
                .toList();
    }


    @SuppressWarnings("unchecked")
    public static <T> T instantiate(Class<T> clazz) {
        T ret;
        if (IterableTypes.isIterableType(clazz)) {
            throw new UnsupportedOperationException("Iterable Instantiation is not supported for: " + clazz.getName());
        }

        if (clazz.isInterface()) {
            Class<?> closestImplementation = ClassloaderExplorer.findClosestImplementation(clazz);
            if (closestImplementation != null) {
                return (T) instantiate(closestImplementation);
            }
            throw new UnsupportedOperationException("Cannot Instantiate Interface: " + clazz.getName());
        }

        try {
            for (Constructor<?> c : clazz.getDeclaredConstructors()) {
                if (c.getParameterTypes().length == 0) {
                    c.setAccessible(true);
                    return (T) c.newInstance();
                }
            }
            if (ReflectiveAccessor.isArray(clazz)) {
                throw new UnsupportedOperationException("Instantiation of Array is not supported");
            } else {
                // WARNING: In theory this is very 'unsafe' however if the SPECS are compliant with the domain model, the required object will be properly instantiated
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                Unsafe unsafe = (Unsafe) f.get(null);
                ret = (T) unsafe.allocateInstance(clazz);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot Instantiate Object of:" + clazz.getName(), e);
        }
        return ret;
    }

}
