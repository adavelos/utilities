package argonath.utils.test;

import argonath.utils.reflection.ObjectFactoryStrategy;
import argonath.utils.reflector.reader.types.CollectionUtils;
import argonath.utils.reflector.reader.types.Numbers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TestTypes {

    @Test
    public void testBigDecimal() {
        Assertions.assertEquals("123", Numbers.bigDecimalToString(new BigDecimal("123")));
        Assertions.assertEquals("123.456", Numbers.bigDecimalToString(new BigDecimal("123.456")));
        Assertions.assertEquals("123", Numbers.bigIntegerToString(new BigInteger("123")));
        // null cases
        Assertions.assertNull(Numbers.bigDecimalToString(null));
        Assertions.assertNull(Numbers.bigIntegerToString(null));
    }

    /*

    public static <T> Collection<T> flatten(Collection<T> rawCollection, ObjectFactoryStrategy strategy) {
        if (rawCollection == null) {
            return null;
        }
        if (rawCollection.isEmpty()) {
            return rawCollection;
        }
        if (!(rawCollection.iterator().next() instanceof Collection)) {
            return rawCollection;
        }

        Collection<Collection<T>> collections = (Collection<Collection<T>>) rawCollection;
        return collections.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(listCollectionSupplier(strategy)));
    }

    public static <T> Collection<T> flatten(Collection<T> rawCollection) {
        return flatten(rawCollection, ObjectFactoryStrategy.DEFAULT_STRATEGY);
    }

    public static <T> List<T> emptyList(ObjectFactoryStrategy strategy) {
        return ReflectiveFactory.instantiateList(strategy.defaultListClass());
    }

    public static <T> Collection<T> emptyCollection(Class<T> collectionClass, ObjectFactoryStrategy strategy) {
        T rawObj = ReflectiveFactory.instantiateCollection(collectionClass, strategy.defaultListClass());
        return (Collection<T>) rawObj;
    }

    public static <T> Collection<T> emptyCollection(T object, ObjectFactoryStrategy strategy) {
        return emptyCollection((Class<T>) object.getClass(), ObjectFactoryStrategy.DEFAULT_STRATEGY);
    }

    public static <T> Collection<T> collectionSupplier(Object collectionObject, ObjectFactoryStrategy strategy) {
        Class<?> clazz = collectionObject.getClass();
        if (ReflectiveAccessor.isList(clazz)) {
            return (List<T>) listCollectionSupplier(clazz, strategy);
        } else if (ReflectiveAccessor.isSet(clazz)) {
            return (Set<T>) setCollectionSupplier(clazz, strategy);
        } else {
            throw new IllegalArgumentException("Unsupported collection type: " + clazz);
        }
    }

    public static <C> Supplier<List<C>> listCollectionSupplier(Class<?> listClass, ObjectFactoryStrategy strategy) {
        Supplier<List<C>> cSupplier = () -> (List<C>) ReflectiveFactory.instantiateList(listClass, strategy.defaultListClass());
        return cSupplier;
    }

    public static <C> Supplier<List<C>> listCollectionSupplier(ObjectFactoryStrategy strategy) {
        Supplier<List<C>> cSupplier = () -> (List<C>) ReflectiveFactory.instantiateList(strategy.defaultListClass());
        return cSupplier;
    }

    public static <C> Supplier<Set<C>> setCollectionSupplier(Class<?> setClass, ObjectFactoryStrategy strategy) {
        Supplier<Set<C>> cSupplier = () -> (Set<C>) ReflectiveFactory.instantiateSet(setClass, strategy.defaultSetClass());
        return cSupplier;
    }
     */

    @Test
    public void testEmptyList() {
        List<?> list = CollectionUtils.emptyList(ObjectFactoryStrategy.DEFAULT_STRATEGY);
        Assertions.assertTrue(list instanceof ArrayList, "list is ArrayList");
        Assertions.assertTrue(list.isEmpty(), "list is empty");

        List<?> list2 = CollectionUtils.emptyList(new ObjectFactoryStrategy(null, LinkedList.class, null));
        Assertions.assertTrue(list2 instanceof LinkedList, "list2 is LinkedList");
        Assertions.assertTrue(list2.isEmpty(), "list2 is empty");
    }
}
