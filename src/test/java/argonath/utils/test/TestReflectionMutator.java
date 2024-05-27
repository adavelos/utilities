package argonath.utils.test;

import argonath.utils.Assert;
import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.reflection.ReflectiveFactory;
import argonath.utils.reflection.ReflectiveMutator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TestReflectionMutator {

    @Test
    public void testFactory() {
        TestClass instance1 = ReflectiveFactory.instantiate(TestClass.class);
        Assert.notNull(instance1, "TestClass instance is not null");

        TestClass2 instance2 = ReflectiveFactory.instantiate(TestClass2.class);
        Assert.notNull(instance2, "TestClass2 instance is not null");

    }

    @Test
    public void testCollectionsFactory() {
        Collection<Object> list = ReflectiveFactory.instantiateCollection(List.class);
        Assert.notNull(list, "List instance is not null");
        Assertions.assertTrue(list instanceof ArrayList, "List is instance of ArrayList");

        List<Object> list1 = ReflectiveFactory.instantiateCollection(List.class, LinkedList.class);
        Assert.notNull(list1, "List instance is not null");
        Assertions.assertTrue(list1 instanceof LinkedList, "List is instance of LinkedList");

        List<Object> list2 = ReflectiveFactory.instantiateCollection(ArrayList.class);
        Assert.notNull(list2, "List instance is not null");
        Assertions.assertTrue(list2 instanceof ArrayList, "List is instance of ArrayList");

        List<Object> list3 = ReflectiveFactory.instantiateCollection(List.class, LinkedList.class);
        Assert.notNull(list3, "List instance is not null");
        Assertions.assertTrue(list3 instanceof LinkedList, "List is instance of LinkedList");

        Set<Object> set = ReflectiveFactory.instantiateCollection(Set.class);
        Assert.notNull(set, "Set instance is not null");
        Assertions.assertTrue(set instanceof HashSet, "Set is instance of HashSet");

        Set<Object> set1 = ReflectiveFactory.instantiateCollection(Set.class, TreeSet.class);
        Assert.notNull(set1, "Set instance is not null");
        Assertions.assertTrue(set1 instanceof TreeSet, "Set is instance of TreeSet");

        // instantiate HashSet
        Set<Object> set2 = ReflectiveFactory.instantiateCollection(HashSet.class);
        Assert.notNull(set2, "Set instance is not null");
        Assertions.assertTrue(set2 instanceof HashSet, "Set is instance of HashSet");

        Set<Object> set3 = ReflectiveFactory.instantiateCollection(Set.class, LinkedHashSet.class);
        Assert.notNull(set3, "Set instance is not null");
        Assertions.assertTrue(set3 instanceof LinkedHashSet, "Set is instance of LinkedHashSet");

        Set<Object> set4 = ReflectiveFactory.instantiateCollection(Set.class, TreeSet.class);
        Assert.notNull(set4, "Set instance is not null");
        Assertions.assertTrue(set4 instanceof TreeSet, "Set is instance of TreeSet");


        Set<Object> set5 = ReflectiveFactory.instantiateCollection(LinkedHashSet.class);
        Assert.notNull(set5, "Set instance is not null");
        Assertions.assertTrue(set5 instanceof LinkedHashSet, "Set is instance of LinkedHashSet");


        Set<Object> set6 = ReflectiveFactory.instantiateCollection(Set.class, HashSet.class);
        Assert.notNull(set6, "Set instance is not null");
        Assertions.assertTrue(set6 instanceof HashSet, "Set is instance of HashSet");

        Set<Object> set7 = ReflectiveFactory.instantiateCollection(TreeSet.class);
        Assert.notNull(set7, "Set instance is not null");
        Assertions.assertTrue(set7 instanceof TreeSet, "Set is instance of TreeSet");

        Map<Object, Object> map = ReflectiveFactory.instantiateMap(Map.class);
        Assert.notNull(map, "Map instance is not null");
        Assertions.assertTrue(map instanceof HashMap, "Map is instance of HashMap");

        Map<Object, Object> map1 = ReflectiveFactory.instantiateMap(LinkedHashMap.class);
        Assert.notNull(map1, "Map instance is not null");
        Assertions.assertTrue(map1 instanceof LinkedHashMap, "Map is instance of LinkedHashMap");

        Map<Object, Object> map3 = ReflectiveFactory.instantiateMap(HashMap.class);
        Assert.notNull(map3, "Map instance is not null");
        Assertions.assertTrue(map3 instanceof HashMap, "Map is instance of HashMap");

        Map<Object, Object> map4 = ReflectiveFactory.instantiateMap(TreeMap.class);
        Assert.notNull(map4, "Map instance is not null");
        Assertions.assertTrue(map4 instanceof TreeMap, "Map is instance of TreeMap");


        Map<Object, Object> map2 = ReflectiveFactory.instantiateMap(Map.class, LinkedHashMap.class);
        Assert.notNull(map2, "Map instance is not null");
        Assertions.assertTrue(map2 instanceof LinkedHashMap, "Map is instance of LinkedHashMap");

        Map<Object, Object> map5 = ReflectiveFactory.instantiateMap(Map.class, HashMap.class);
        Assert.notNull(map5, "Map instance is not null");
        Assertions.assertTrue(map5 instanceof HashMap, "Map is instance of HashMap");

        Map<Object, Object> map6 = ReflectiveFactory.instantiateMap(Map.class, TreeMap.class);
        Assert.notNull(map6, "Map instance is not null");
        Assertions.assertTrue(map6 instanceof TreeMap, "Map is instance of TreeMap");

        Object[] array = ReflectiveFactory.instantiateArray(Object[].class, 10);
        Assert.notNull(array, "Array instance is not null");
        Assertions.assertEquals(10, array.length, "Array length is 10");
    }


    @Test
    public void testMutator() {
        TestMutatorClass sample = new TestMutatorClass("string", 1, 2L, 3.0f, 4.0, true, new BigDecimal(5), new BigInteger("6"), new Object());
        ;
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("stringField", TestMutatorClass.class), sample, "sampleString");
        Assertions.assertEquals("sampleString", sample.stringField, "String field is set");
        // Integer
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("integerField", TestMutatorClass.class), sample, 10);
        Assertions.assertEquals(10, (int) sample.integerField, "Integer field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("integerField", TestMutatorClass.class), sample, "10");
        Assertions.assertEquals(10, (int) sample.integerField, "Integer field is set");
        // Long
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("longField", TestMutatorClass.class), sample, 20L);
        Assertions.assertEquals(20L, (long) sample.longField, "Long field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("longField", TestMutatorClass.class), sample, "20");
        Assertions.assertEquals(20L, (long) sample.longField, "Long field is set");
        // Float
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("floatField", TestMutatorClass.class), sample, 30.0f);
        Assertions.assertEquals(30.0f, sample.floatField, "Float field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("floatField", TestMutatorClass.class), sample, "30.0");
        Assertions.assertEquals(30.0f, sample.floatField, "Float field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("floatField", TestMutatorClass.class), sample, "30");
        Assertions.assertEquals(30.0f, sample.floatField, "Float field is set");
        // Double
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("doubleField", TestMutatorClass.class), sample, 40.0);
        Assertions.assertEquals(40.0, sample.doubleField, "Double field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("doubleField", TestMutatorClass.class), sample, "40.0");
        Assertions.assertEquals(40.0, sample.doubleField, "Double field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("doubleField", TestMutatorClass.class), sample, "40");
        Assertions.assertEquals(40.0, sample.doubleField, "Double field is set");
        // Boolean
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("booleanField", TestMutatorClass.class), sample, false);
        Assertions.assertFalse(sample.booleanField, "Boolean field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("booleanField", TestMutatorClass.class), sample, "false");
        Assertions.assertFalse(sample.booleanField, "Boolean field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("booleanField", TestMutatorClass.class), sample, "true");
        Assertions.assertTrue(sample.booleanField, "Boolean field is set");
        // BigDecimal
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("bigDecimalField", TestMutatorClass.class), sample, new BigDecimal(50));
        Assertions.assertEquals(sample.bigDecimalField, new BigDecimal(50), "BigDecimal field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("bigDecimalField", TestMutatorClass.class), sample, "50");
        Assertions.assertEquals(sample.bigDecimalField, new BigDecimal(50), "BigDecimal field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("bigDecimalField", TestMutatorClass.class), sample, "50.012");
        Assertions.assertEquals(sample.bigDecimalField, new BigDecimal("50.012"), "BigDecimal field is set");
        // BigInteger
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("bigIntegerField", TestMutatorClass.class), sample, new BigInteger("60"));
        Assertions.assertEquals(sample.bigIntegerField, new BigInteger("60"), "BigInteger field is set");
        ReflectiveMutator.setFieldValue(ReflectiveAccessor.getField("bigIntegerField", TestMutatorClass.class), sample, "60");
        Assertions.assertEquals(sample.bigIntegerField, new BigInteger("60"), "BigInteger field is set");

    }

    private static class TestClass {
        private String name;
        private int age;
        private List<String> friends;
        private byte[] data;

        public TestClass(String name, int age, List<String> friends, byte[] data) {
            this.name = name;
            this.age = age;
            this.friends = friends;
            this.data = data;
        }
    }

    private static class TestClass2 {
        private String name;
    }

    public class TestMutatorClass {
        private String stringField;
        private Integer integerField;
        private Long longField;
        private Float floatField;
        private Double doubleField;
        private Boolean booleanField;
        private BigDecimal bigDecimalField;
        private BigInteger bigIntegerField;
        private Object objectField;

        public TestMutatorClass() {
        }

        public TestMutatorClass(String stringField, Integer integerField, Long longField, Float floatField, Double doubleField, Boolean booleanField, BigDecimal bigDecimalField, BigInteger bigIntegerField, Object objectField) {
            this.stringField = stringField;
            this.integerField = integerField;
            this.longField = longField;
            this.floatField = floatField;
            this.doubleField = doubleField;
            this.booleanField = booleanField;
            this.bigDecimalField = bigDecimalField;
            this.bigIntegerField = bigIntegerField;
            this.objectField = objectField;
        }
    }
}
