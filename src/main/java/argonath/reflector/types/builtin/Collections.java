package argonath.reflector.types.builtin;

import argonath.reflector.factory.ObjectFactory;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.utils.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

public class Collections {
    private Collections() {
    }

    public static <T> List<T> emptyList() {
        return ObjectFactory.create(List.class);
    }

    public static <T> List<T> asList(Object listObject) {
        Assert.isTrue(ReflectiveAccessor.isList(listObject), "Object is not a list: " + listObject + " (" + listObject.getClass() + ")");
        return (List<T>) listObject;
    }


    public static Collection<Object> asCollection(Object object) {
        Assert.notNull(object, "Object cannot be null");
        return (Collection<Object>) object;
    }

    public static Collection fromArrayOfPrimitives(Object object) {
        if (object instanceof int[] intArray) {
            return Arrays.stream(intArray).boxed().toList();
        } else if (object instanceof long[] longArray) {
            return Arrays.stream(longArray).boxed().toList();
        } else if (object instanceof double[] doubleArray) {
            return Arrays.stream(doubleArray).boxed().toList();
        } else if (object instanceof float[] floatArray) {
            return IntStream.range(0, floatArray.length)
                    .mapToObj(i -> floatArray[i])
                    .toList();
        } else if (object instanceof short[] shortArray) {
            return IntStream.range(0, shortArray.length)
                    .mapToObj(i -> shortArray[i])
                    .toList();
        } else if (object instanceof byte[] byteArray) {
            return IntStream.range(0, byteArray.length)
                    .mapToObj(i -> byteArray[i])
                    .toList();
        } else if (object instanceof char[] charArray) {
            return IntStream.range(0, charArray.length)
                    .mapToObj(i -> charArray[i])
                    .toList();
        } else if (object instanceof boolean[] booleanArray) {
            return IntStream.range(0, booleanArray.length)
                    .mapToObj(i -> booleanArray[i])
                    .toList();
        } else {
            throw new IllegalArgumentException("Object is not an array of primitives: " + object);
        }
    }
}
