package argonath.utils.test.generator;

import argonath.reflector.generator.ObjectGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class TestObjectGeneratorSpecs {

    @Test
    void testWithSpecsFile() {
        Outer obj = ObjectGenerator.create(Outer.class)
                .withSpecsFile("test.class.specs")
                .generate();

        // Assert Outer class fields
        Assertions.assertTrue(obj.intField >= 5 && obj.intField <= 10);
        Assertions.assertTrue(obj.stringField.length() >= 10 && obj.stringField.length() <= 20);
        Assertions.assertTrue(obj.intArray.length >= 3 && obj.intArray.length <= 5);
        Arrays.stream(obj.intArray).forEach(i -> Assertions.assertTrue(i >= 50 && i <= 100));
        Assertions.assertTrue(obj.stringArray.length >= 2 && obj.stringArray.length <= 4);
        Arrays.stream(obj.stringArray).forEach(s -> Assertions.assertEquals(10, s.length()));
        Assertions.assertTrue(obj.stringList.size() >= 4 && obj.stringList.size() <= 8);
        obj.stringList.forEach(s -> Assertions.assertTrue(s.length() >= 8 && s.length() <= 15));
        Assertions.assertTrue(obj.stringMap.size() >= 2 && obj.stringMap.size() <= 5);
        obj.stringMap.forEach((k, v) -> {
            Assertions.assertTrue(k.length() >= 5 && k.length() <= 10);
            Assertions.assertTrue(v.length() >= 5 && v.length() <= 10);
        });
        Assertions.assertTrue(obj.intList.size() >= 3 && obj.intList.size() <= 6);
        obj.intList.forEach(i -> Assertions.assertTrue(i >= 1 && i <= 100));

        // TODO: Map special handling: Key generation should produce distinct keys:
        //  Assertions.assertTrue(obj.intMap.size() >= 2 && obj.intMap.size() <= 4);
        obj.intMap.forEach((k, v) -> {
            Assertions.assertTrue(k >= 1 && k <= 50);
            Assertions.assertTrue(v >= 1 && v <= 50);
        });

        Assertions.assertNull(obj.toBeIgnored);

        // Assert Inner class fields
        Assertions.assertNotNull(obj.inner);
        Assertions.assertTrue(obj.inner.intField >= 1 && obj.inner.intField <= 20);
        Assertions.assertTrue(obj.inner.stringField.length() >= 5 && obj.inner.stringField.length() <= 15);
        Assertions.assertTrue(obj.inner.intArray.length >= 3 && obj.inner.intArray.length <= 6);
        Arrays.stream(obj.inner.intArray).forEach(i -> Assertions.assertTrue(i >= 10 && i <= 30));
        Assertions.assertTrue(obj.inner.stringArray.length >= 2 && obj.inner.stringArray.length <= 5);
        Arrays.stream(obj.inner.stringArray).forEach(s -> Assertions.assertEquals(7, s.length()));
        Assertions.assertTrue(obj.inner.stringList.size() >= 3 && obj.inner.stringList.size() <= 5);
        obj.inner.stringList.forEach(s -> Assertions.assertTrue(s.length() >= 6 && s.length() <= 12));
        Assertions.assertTrue(obj.inner.intList.size() >= 2 && obj.inner.intList.size() <= 4);
        obj.inner.intList.forEach(i -> Assertions.assertTrue(i >= 1 && i <= 40));

        Assertions.assertNull(obj.innerNone);
        Assertions.assertEquals("fixed_value", obj.fixedValue);
        Assertions.assertEquals(100, obj.intFixedValue);

        // Assert innerList
        if (obj.innerList != null) {
            Assertions.assertTrue(obj.innerList.size() >= 3 && obj.innerList.size() <= 4);
            obj.innerList.forEach(inner -> {
                Assertions.assertTrue(inner.intField >= 1 && inner.intField <= 15);
                Assertions.assertTrue(inner.stringField.length() >= 8 && inner.stringField.length() <= 12);
                Assertions.assertTrue(inner.intArray.length >= 2 && inner.intArray.length <= 4);
                Arrays.stream(inner.intArray).forEach(i -> Assertions.assertTrue(i >= 5 && i <= 25));
                Assertions.assertTrue(inner.stringArray.length >= 1 && inner.stringArray.length <= 3);
                Arrays.stream(inner.stringArray).forEach(s -> Assertions.assertEquals(5, s.length()));
                Assertions.assertTrue(inner.stringList.size() >= 2 && inner.stringList.size() <= 3);
                inner.stringList.forEach(s -> Assertions.assertTrue(s.length() >= 4 && s.length() <= 8));
                Assertions.assertTrue(inner.intList.size() >= 1 && inner.intList.size() <= 3);
                inner.intList.forEach(i -> Assertions.assertTrue(i >= 1 && i <= 30));
            });
        }

        // Assert innerMap
        if (obj.innerMap != null) {
            Assertions.assertTrue(obj.innerMap.size() >= 2 && obj.innerMap.size() <= 3);
            obj.innerMap.forEach((k, inner) -> {
                Assertions.assertTrue(inner.intField >= 1 && inner.intField <= 15);
                Assertions.assertTrue(inner.stringField.length() >= 8 && inner.stringField.length() <= 12);
                Assertions.assertTrue(inner.intArray.length >= 5 && inner.intArray.length <= 10);
                Arrays.stream(inner.intArray).forEach(i -> Assertions.assertTrue(i >= 5 && i <= 25));
                Assertions.assertTrue(inner.stringArray.length >= 7 && inner.stringArray.length <= 14);
                Arrays.stream(inner.stringArray).forEach(s -> Assertions.assertEquals(5, s.length()));
                Assertions.assertTrue(inner.stringList.size() >= 6 && inner.stringList.size() <= 9);
                inner.stringList.forEach(s -> Assertions.assertTrue(s.length() >= 4 && s.length() <= 8));
                Assertions.assertTrue(inner.intList.size() >= 1 && inner.intList.size() <= 3);
                inner.intList.forEach(i -> Assertions.assertTrue(i >= 1 && i <= 30));
            });
        }

        // TODO: To be enabled when Map generator is supported: Assertions.assertTrue(k >= 1 && k <= 20);
//        if (obj.intStringMap != null) {
//            Assertions.assertTrue(obj.intStringMap.size() >= 2 && obj.intStringMap.size() <= 4);
//            obj.intStringMap.forEach((k, v) -> {
//                Assertions.assertTrue(v.length() >= 5 && v.length() <= 10);
//            });
//        }

        // TODO: To be enabled when Map generator is supported: Assertions.assertTrue(k >= 1 && k <= 20);
//        if (obj.stringIntMap != null) {
//            Assertions.assertTrue(obj.stringIntMap.size() >= 2 && obj.stringIntMap.size() <= 4);
//            obj.stringIntMap.forEach((k, v) -> {
//                Assertions.assertTrue(k.length() >= 5 && k.length() <= 10);
//                //Assertions.assertTrue(v >= 1 && v <= 20);
//            });
//        }


    }

    private static class Outer {
        int intField;

        String stringField;
        int[] intArray;
        String[] stringArray;
        List<String> stringList;
        Map<String, String> stringMap;
        List<Integer> intList;
        Map<Integer, Integer> intMap;
     //   Map<Integer, String> intStringMap;
     //   Map<String, Integer> stringIntMap;
        String toBeIgnored;

        Inner inner;
        Inner innerNone;
        List<Inner> innerList;
        Map<String, Inner> innerMap;

        String fixedValue;
        Integer intFixedValue;
    }

    private static class Inner {
        int intField;
        String stringField;
        int[] intArray;
        String[] stringArray;
        List<String> stringList;
        List<Integer> intList;
    }

}
