package argonath.utils.test.generator;

import argonath.reflector.config.Configuration;
import argonath.reflector.generator.ObjectGenerator;
import argonath.utils.test.generator.model.GenericClass;
import argonath.utils.test.generator.model.InnerTestClass;
import argonath.utils.test.generator.model.TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestObjectGeneratorDefaults {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testGenerateDefault() {
        for (int i = 0; i < 100; i++) {
            TestClass generate = ObjectGenerator.create(TestClass.class)
                    .withAllMandatory()
                    .generate();

            // Assert that each field is populated
            assertInnerClass(generate.stringField, generate.integerWrapperField, generate.floatWrapperField, generate.doubleWrapperField, generate.longWrapperField, generate.charWrapperField, generate.byteWrapperField, generate.shortWrapperField, generate.booleanWrapperField, generate.localDateField, generate.localDateTimeField, generate.offsetDateTimeField, generate.offsetTimeField, generate.zonedDateTimeField, generate.bigIntegerField, generate.bigDecimalField, generate.byteArrayField, generate.intField, generate.floatField, generate.doubleField, generate.longField, generate.charField, generate.byteField, generate.shortField);

            // List
            List<String> listField = generate.stringListField;
            Assertions.assertTrue(listField != null && listField.size() >= 1 && listField.size() <= 3);
            Assertions.assertTrue(listField.stream().allMatch(s -> s != null && s.length() >= 5 && s.length() <= 10));

            // Set
            Set<String> setField = generate.stringSetField;
            Assertions.assertTrue(setField != null && setField.size() >= 1 && setField.size() <= 3);
            Assertions.assertTrue(setField.stream().allMatch(s -> s != null && s.length() >= 5 && s.length() <= 10));

            // Array
            String[] stringArrayField = generate.stringArrayField;
            Assertions.assertTrue(stringArrayField != null && stringArrayField.length >= 1 && stringArrayField.length <= 3);
            Assertions.assertTrue(stringArrayField.length >= 1 && stringArrayField.length <= 3);


            // Test for int array
            int[] intArrayField = generate.intArrayField;
            Assertions.assertNotNull(intArrayField);
            Assertions.assertTrue(intArrayField.length >= 1 && intArrayField.length <= 3);

            // Test for short array
            short[] shortArrayField = generate.shortArrayField;
            Assertions.assertNotNull(shortArrayField);
            Assertions.assertTrue(shortArrayField.length >= 1 && shortArrayField.length <= 3);

            // Test for boolean array
            boolean[] booleanArrayField = generate.booleanArrayField;
            Assertions.assertNotNull(booleanArrayField);
            Assertions.assertTrue(booleanArrayField.length >= 1 && booleanArrayField.length <= 3);

            // Test for float array
            float[] floatArrayField = generate.floatArrayField;
            Assertions.assertNotNull(floatArrayField);
            Assertions.assertTrue(floatArrayField.length >= 1 && floatArrayField.length <= 3);

            // Test for double array
            double[] doubleArrayField = generate.doubleArrayField;
            Assertions.assertNotNull(doubleArrayField);
            Assertions.assertTrue(doubleArrayField.length >= 1 && doubleArrayField.length <= 3);

            // Test for char array
            char[] charArrayField = generate.charArrayField;
            Assertions.assertNotNull(charArrayField);
            Assertions.assertTrue(charArrayField.length >= 1 && charArrayField.length <= 3);

            // List of Complex
            List<InnerTestClass> innerTestClassList = generate.innerTestClassList;
            Assertions.assertTrue(innerTestClassList != null && innerTestClassList.size() >= 1 && innerTestClassList.size() <= 3);
            innerTestClassList.forEach(innerTestClassInstance -> {
                assertInnerClass(innerTestClassInstance.innerStringField, innerTestClassInstance.innerIntegerWrapperField, innerTestClassInstance.innerFloatWrapperField, innerTestClassInstance.innerDoubleWrapperField, innerTestClassInstance.innerLongWrapperField, innerTestClassInstance.innerCharWrapperField, innerTestClassInstance.innerByteWrapperField, innerTestClassInstance.innerShortWrapperField, innerTestClassInstance.innerBooleanWrapperField, innerTestClassInstance.innerLocalDateField, innerTestClassInstance.innerLocalDateTimeField, innerTestClassInstance.innerOffsetDateTimeField, innerTestClassInstance.innerOffsetTimeField, innerTestClassInstance.innerZonedDateTimeField, innerTestClassInstance.innerBigIntegerField, innerTestClassInstance.innerBigDecimalField, innerTestClassInstance.innerByteArrayField, innerTestClassInstance.innerIntField, innerTestClassInstance.innerFloatField, innerTestClassInstance.innerDoubleField, innerTestClassInstance.innerLongField, innerTestClassInstance.innerCharField, innerTestClassInstance.innerByteField, innerTestClassInstance.innerShortField);
            });

            // Set of Complex
            Set<InnerTestClass> innerTestClassSet = generate.innerTestClassSet;
            Assertions.assertTrue(innerTestClassSet != null && innerTestClassSet.size() >= 1 && innerTestClassSet.size() <= 3);
            innerTestClassSet.forEach(innerTestClassInstance -> {
                assertInnerClass(innerTestClassInstance.innerStringField, innerTestClassInstance.innerIntegerWrapperField, innerTestClassInstance.innerFloatWrapperField, innerTestClassInstance.innerDoubleField, innerTestClassInstance.innerLongField, innerTestClassInstance.innerCharField, innerTestClassInstance.innerByteField, innerTestClassInstance.innerShortField, innerTestClassInstance.innerBooleanField, innerTestClassInstance.innerLocalDateField, innerTestClassInstance.innerLocalDateTimeField, innerTestClassInstance.innerOffsetDateTimeField, innerTestClassInstance.innerOffsetTimeField, innerTestClassInstance.innerZonedDateTimeField, innerTestClassInstance.innerBigIntegerField, innerTestClassInstance.innerBigDecimalField, innerTestClassInstance.innerByteArrayField, innerTestClassInstance.innerIntField, innerTestClassInstance.innerFloatField, innerTestClassInstance.innerDoubleField, innerTestClassInstance.innerLongField, innerTestClassInstance.innerCharField, innerTestClassInstance.innerByteField, innerTestClassInstance.innerShortField);
            });

            // Array of Complex
            InnerTestClass[] innerTestClassArray = generate.innerTestClassArray;
            Assertions.assertTrue(innerTestClassArray != null && innerTestClassArray.length >= 1 && innerTestClassArray.length <= 3);
            for (InnerTestClass innerTestClassInstance : innerTestClassArray) {
                assertInnerClass(innerTestClassInstance.innerStringField, innerTestClassInstance.innerIntegerWrapperField, innerTestClassInstance.innerFloatWrapperField, innerTestClassInstance.innerDoubleField, innerTestClassInstance.innerLongField, innerTestClassInstance.innerCharField, innerTestClassInstance.innerByteField, innerTestClassInstance.innerShortField, innerTestClassInstance.innerBooleanField, innerTestClassInstance.innerLocalDateField, innerTestClassInstance.innerLocalDateTimeField, innerTestClassInstance.innerOffsetDateTimeField, innerTestClassInstance.innerOffsetTimeField, innerTestClassInstance.innerZonedDateTimeField, innerTestClassInstance.innerBigIntegerField, innerTestClassInstance.innerBigDecimalField, innerTestClassInstance.innerByteArrayField, innerTestClassInstance.innerIntField, innerTestClassInstance.innerFloatField, innerTestClassInstance.innerDoubleField, innerTestClassInstance.innerLongField, innerTestClassInstance.innerCharField, innerTestClassInstance.innerByteField, innerTestClassInstance.innerShortField);
            }

            Map<String, InnerTestClass> innerTestClassMap = generate.innerTestClassMap;
            Assertions.assertTrue(innerTestClassMap != null && innerTestClassMap.size() >= 1 && innerTestClassMap.size() <= 3);
            innerTestClassMap.forEach((key, value) -> {
                Assertions.assertTrue(key != null && key.length() >= 5 && key.length() <= 10);
                assertInnerClass(value.innerStringField, value.innerIntegerWrapperField, value.innerFloatWrapperField, value.innerDoubleField, value.innerLongField, value.innerCharField, value.innerByteField, value.innerShortField, value.innerBooleanField, value.innerLocalDateField, value.innerLocalDateTimeField, value.innerOffsetDateTimeField, value.innerOffsetTimeField, value.innerZonedDateTimeField, value.innerBigIntegerField, value.innerBigDecimalField, value.innerByteArrayField, value.innerIntField, value.innerFloatField, value.innerDoubleField, value.innerLongField, value.innerCharField, value.innerByteField, value.innerShortField);
            });


            Map<String, String> stringMapField = generate.stringMapField;
            Assertions.assertTrue(stringMapField != null && stringMapField.size() >= 1 && stringMapField.size() <= 3);
            stringMapField.forEach((key, value) -> {
                Assertions.assertTrue(key != null && key.length() >= 5 && key.length() <= 10);
                Assertions.assertTrue(value != null && value.length() >= 5 && value.length() <= 10);
            });

            // Advanced Structures (require implementation of Type explorer)
            List<List<List<String>>> nestedStringListField = generate.nestedStringListField;
            Assertions.assertTrue(nestedStringListField != null && nestedStringListField.size() >= 1 && nestedStringListField.size() <= 3);
            nestedStringListField.forEach(listList -> {
                Assertions.assertTrue(listList != null && listList.size() >= 1 && listList.size() <= 3);
                listList.forEach(list -> {
                    Assertions.assertTrue(list != null && list.size() >= 1 && list.size() <= 3);
                    list.forEach(s -> {
                        Assertions.assertTrue(s != null && s.length() >= 5 && s.length() <= 10);
                    });
                });
            });

            Map<String, Map<String, Map<String, Map<String, Integer>>>> nestedMapField = generate.nestedMapField;
            Assertions.assertTrue(nestedMapField != null && nestedMapField.size() >= 1 && nestedMapField.size() <= 3);
            nestedMapField.forEach((key, value) -> {
                Assertions.assertTrue(key != null && key.length() >= 5 && key.length() <= 10);
                Assertions.assertTrue(value != null && value.size() >= 1 && value.size() <= 3);
                value.forEach((key1, value1) -> {
                    Assertions.assertTrue(key1 != null && key1.length() >= 5 && key1.length() <= 10);
                    Assertions.assertTrue(value1 != null && value1.size() >= 1 && value1.size() <= 3);
                    value1.forEach((key2, value2) -> {
                        Assertions.assertTrue(key2 != null && key2.length() >= 5 && key2.length() <= 10);
                        Assertions.assertTrue(value2 != null && value2.size() >= 1 && value2.size() <= 3);
                        value2.forEach((key3, value3) -> {
                            Assertions.assertTrue(key3 != null && key3.length() >= 5 && key3.length() <= 10);
                            Assertions.assertTrue(value3 != null && value3 >= 1 && value3 <= 100);
                        });
                    });
                });
            });

            List<String>[] stringListArrayField = generate.stringListArrayField;
            Assertions.assertTrue(stringListArrayField != null && stringListArrayField.length >= 1 && stringListArrayField.length <= 3);
            for (List<String> list : stringListArrayField) {
                Assertions.assertTrue(list != null && list.size() >= 1 && list.size() <= 3);
                list.forEach(s -> {
                    Assertions.assertTrue(s != null && s.length() >= 5 && s.length() <= 10);
                });
            }

            Map<String, List<String>>[] stringMapArrayField = generate.stringMapArrayField;
            Assertions.assertTrue(stringMapArrayField != null && stringMapArrayField.length >= 1 && stringMapArrayField.length <= 3);
            for (Map<String, List<String>> map : stringMapArrayField) {
                Assertions.assertTrue(map != null && map.size() >= 1 && map.size() <= 3);
                map.forEach((key, value) -> {
                    Assertions.assertTrue(key != null && key.length() >= 5 && key.length() <= 10);
                    Assertions.assertTrue(value != null && value.size() >= 1 && value.size() <= 3);
                    value.forEach(s -> {
                        Assertions.assertTrue(s != null && s.length() >= 5 && s.length() <= 10);
                    });
                });
            }

            List<? extends Number> wildcardListField = generate.wildcardListField;
            Assertions.assertTrue(wildcardListField != null && wildcardListField.size() >= 1 && wildcardListField.size() <= 3);
            wildcardListField.forEach(number -> {
                Assertions.assertTrue(number != null);
            });


            GenericClass<String> genericClassField = generate.genericClassField;
            Assertions.assertTrue(genericClassField != null);
            Assertions.assertTrue(genericClassField.field != null && genericClassField.field.length() >= 5 && genericClassField.field.length() <= 10);

            int[][] intArrayField2D = generate.intArrayField2D;
            Assertions.assertTrue(intArrayField2D != null && intArrayField2D.length >= 1 && intArrayField2D.length <= 3);
            for (int[] ints : intArrayField2D) {
                Assertions.assertTrue(ints != null && ints.length >= 1 && ints.length <= 3);
            }

            int[][][] intArrayField3D = generate.intArrayField3D;
            Assertions.assertTrue(intArrayField3D != null && intArrayField3D.length >= 1 && intArrayField3D.length <= 3);
            for (int[][] ints : intArrayField3D) {
                Assertions.assertTrue(ints != null && ints.length >= 1 && ints.length <= 3);
                for (int[] anInt : ints) {
                    Assertions.assertTrue(anInt != null && anInt.length >= 1 && anInt.length <= 3);
                }
            }

            String[][] stringArrayField2D = generate.stringArrayField2D;
            Assertions.assertTrue(stringArrayField2D != null && stringArrayField2D.length >= 1 && stringArrayField2D.length <= 3);
            for (String[] strings : stringArrayField2D) {
                Assertions.assertTrue(strings != null && strings.length >= 1 && strings.length <= 3);
            }

            String[][][] stringArrayField3D = generate.stringArrayField3D;
            Assertions.assertTrue(stringArrayField3D != null && stringArrayField3D.length >= 1 && stringArrayField3D.length <= 3);
            for (String[][] strings : stringArrayField3D) {
                Assertions.assertTrue(strings != null && strings.length >= 1 && strings.length <= 3);
                for (String[] strings1 : strings) {
                    Assertions.assertTrue(strings1 != null && strings1.length >= 1 && strings1.length <= 3);
                }
            }
        }

    }

    private static void assertInnerClass(String innerTestClassInstance, Integer innerTestClassInstance1, Float innerTestClassInstance2, Double innerTestClassInstance3, Long innerTestClassInstance4, Character innerTestClassInstance5, Byte innerTestClassInstance6, Short innerTestClassInstance7, Boolean innerTestClassInstance8, LocalDate innerTestClassInstance9, LocalDateTime innerTestClassInstance10, OffsetDateTime innerTestClassInstance11, OffsetTime innerTestClassInstance12, ZonedDateTime innerTestClassInstance13, BigInteger innerTestClassInstance14, BigDecimal innerTestClassInstance15, byte[] innerTestClassInstance16, int innerTestClassInstance17, float innerTestClassInstance18, double innerTestClassInstance19, long innerTestClassInstance20, char innerTestClassInstance21, byte innerTestClassInstance22, short innerTestClassInstance23) {
        // Assert that each field is populated
        Assertions.assertNotNull(innerTestClassInstance);
        Assertions.assertNotNull(innerTestClassInstance1);
        Assertions.assertNotNull(innerTestClassInstance2);
        Assertions.assertNotNull(innerTestClassInstance3);
        Assertions.assertNotNull(innerTestClassInstance4);
        Assertions.assertNotNull(innerTestClassInstance5);
        Assertions.assertNotNull(innerTestClassInstance6);
        Assertions.assertNotNull(innerTestClassInstance7);
        Assertions.assertNotNull(innerTestClassInstance8);
        Assertions.assertNotNull(innerTestClassInstance9);
        Assertions.assertNotNull(innerTestClassInstance10);
        Assertions.assertNotNull(innerTestClassInstance11);
        Assertions.assertNotNull(innerTestClassInstance12);
        Assertions.assertNotNull(innerTestClassInstance13);
        Assertions.assertNotNull(innerTestClassInstance14);
        Assertions.assertNotNull(innerTestClassInstance15);
        Assertions.assertNotNull(innerTestClassInstance16);

        // Assert that each primitive field is populated
        Assertions.assertNotEquals(0, innerTestClassInstance17);
        Assertions.assertNotEquals(0.0f, innerTestClassInstance18);
        Assertions.assertNotEquals(0.0, innerTestClassInstance19);
        Assertions.assertNotEquals(0L, innerTestClassInstance20);
        Assertions.assertNotEquals('\u0000', innerTestClassInstance21);
        Assertions.assertNotEquals((byte) 0, innerTestClassInstance22);
        Assertions.assertNotEquals((short) 0, innerTestClassInstance23);
    }

}
