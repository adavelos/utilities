package argonath.utils.test;

import argonath.utils.Assert;
import argonath.utils.reflection.ReflectiveAccessor;
import argonath.utils.reflection.ReflectiveFactory;
import argonath.utils.reflection.ReflectiveMutator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class TestReflectionMutator {

    @Test
    public void testFactory() {
        TestClass instance1 = ReflectiveFactory.instantiate(TestClass.class);
        Assert.notNull(instance1, "TestClass instance is not null");

        TestClass2 instance2 = ReflectiveFactory.instantiate(TestClass2.class);
        Assert.notNull(instance2, "TestClass2 instance is not null");

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
