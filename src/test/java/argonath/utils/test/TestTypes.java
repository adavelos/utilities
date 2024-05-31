package argonath.utils.test;

import argonath.utils.reflection.ObjectFactoryStrategy;
import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.reflection.ReflectiveFactory;
import argonath.utils.reflector.reader.types.Collections;
import argonath.utils.reflector.reader.types.Numbers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    public void testFlatten() {
        List<?> inp1 = List.of("test");
        List<?> out1 = Collections.flatten(inp1);

        Assertions.assertTrue(out1 instanceof List, "out1 is List");
        Assertions.assertEquals(1, out1.size(), "out1 has one element");
        Assertions.assertEquals("test", out1.iterator().next(), "out1 has element 'test'");

        List<?> inp2 = List.of("test", "test2");
        List<?> out2 = Collections.flatten(inp2);
        Assertions.assertTrue(out2 instanceof List, "out2 is ArrayList");
        Assertions.assertEquals(2, out2.size(), "out2 has two elements");
        Iterator<?> it2 = out2.iterator();
        Assertions.assertEquals("test", it2.next(), "out2 has element 'test'");
        Assertions.assertEquals("test2", it2.next(), "out2 has element 'test2'");

        List<?> inp3 = List.of(List.of("test1", "test2"), List.of("test3", "test4"));
        List<?> out3 = Collections.flatten(inp3);
        Assertions.assertTrue(out3 instanceof ArrayList, "out3 is ArrayList");
        Assertions.assertEquals(4, out3.size(), "out3 has four elements");
        Iterator<?> it3 = out3.iterator();
        Assertions.assertEquals("test1", it3.next(), "out3 has element 'test1'");
        Assertions.assertEquals("test2", it3.next(), "out3 has element 'test2'");
        Assertions.assertEquals("test3", it3.next(), "out3 has element 'test3'");
        Assertions.assertEquals("test4", it3.next(), "out3 has element 'test4'");

    }

    @Test
    public void testEmptyList() {
        List<?> list = Collections.emptyList(ObjectFactoryStrategy.DEFAULT_STRATEGY);
        Assertions.assertTrue(list instanceof ArrayList, "list is ArrayList");
        Assertions.assertTrue(list.isEmpty(), "list is empty");

        List<?> list2 = Collections.emptyList(new ObjectFactoryStrategy(null, LinkedList.class, null));
        Assertions.assertTrue(list2 instanceof LinkedList, "list2 is LinkedList");
        Assertions.assertTrue(list2.isEmpty(), "list2 is empty");
    }
}
