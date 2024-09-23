package argonath.utils.test.generator;

import argonath.reflector.generator.FieldSelector;
import argonath.reflector.generator.Generators;
import argonath.reflector.generator.ObjectGenerator;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.generator.model.ObjectSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestSequenceGenerators {

    @Test
    void testSequenceGenerators() {

        // register local generator at: /a and at /innerClass/a
        // register global generator "key1" at /b and at /innerClass/b

        Generator<Integer> gen1 = Generators.sequence(1, 1);
        Generator<Integer> gen2 = Generators.sequence(1, 1);
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/a"), ObjectSpecs.generator(Integer.class, gen1))
                .withSpecs(FieldSelector.ofPath("/inner/a"), ObjectSpecs.generator(Integer.class, gen1))
                .withSpecs(FieldSelector.ofPath("/b"), ObjectSpecs.generator(Integer.class, Generators.sequence(1, 1)))
                .withSpecs(FieldSelector.ofPath("/inner/b"), ObjectSpecs.generator(Integer.class, Generators.sequence(1, 1)))

                .withSpecs(FieldSelector.ofPath("/intArray"), ObjectSpecs.generator(Integer.class, gen2).size(5, 10))
                .withSpecs(FieldSelector.ofPath("/inner/intArray"), ObjectSpecs.generator(Integer.class, gen2).size(5, 10))

                .withSpecs(FieldSelector.ofPath("/integerArray"), ObjectSpecs.generator(Integer.class, Generators.sequence(1, 1)).size(5, 10))
                .withSpecs(FieldSelector.ofPath("/inner/integerArray"), ObjectSpecs.generator(Integer.class, Generators.sequence(1, 1)).size(5, 10))
                .generate();

        // Test global sequence with key "key1"
        Assertions.assertEquals(1, obj.a);
        Assertions.assertEquals(2, obj.inner.a);

        // Test local sequences (unique per path)
        Assertions.assertEquals(1, obj.b);
        Assertions.assertEquals(1, obj.inner.b);

        // Test global sequence with key "key2" for intArray and inner.intArray
        Assertions.assertNotNull(obj.inner.intArray);
        Assertions.assertTrue(obj.inner.intArray.length >= 5 && obj.inner.intArray.length <= 10);
        for (int i = 0; i < obj.inner.intArray.length; i++) {
            Assertions.assertEquals(i + 1, obj.inner.intArray[i]);
        }

        Assertions.assertNotNull(obj.intArray);
        Assertions.assertTrue(obj.intArray.length >= 5 && obj.intArray.length <= 10);
        int expectedStart = obj.inner.intArray.length + 1;
        for (int i = 0; i < obj.intArray.length; i++) {
            Assertions.assertEquals(expectedStart + i, obj.intArray[i]);
        }

        // Test local sequences for integerArray and inner.integerArray
        Assertions.assertNotNull(obj.inner.integerArray);
        Assertions.assertTrue(obj.inner.integerArray.length >= 5 && obj.inner.integerArray.length <= 10);
        for (int i = 0; i < obj.inner.integerArray.length; i++) {
            Assertions.assertEquals(i + 1, obj.inner.integerArray[i]);
        }

        Assertions.assertNotNull(obj.integerArray);
        Assertions.assertTrue(obj.integerArray.length >= 5 && obj.integerArray.length <= 10);
        for (int i = 0; i < obj.integerArray.length; i++) {
            Assertions.assertEquals(i + 1, obj.integerArray[i]);
        }
    }

    @Test
    void testSequenceGeneratorsWithDifferentStartsAndSteps() {
        Generator<Integer> gen1 = Generators.sequence(10, 5);
        Generator<Integer> gen2 = Generators.sequence(1000, 100);
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/a"), ObjectSpecs.generator(Integer.class, gen1))
                .withSpecs(FieldSelector.ofPath("/inner/a"), ObjectSpecs.generator(Integer.class, gen1))
                .withSpecs(FieldSelector.ofPath("/b"), ObjectSpecs.generator(Integer.class, Generators.sequence(100, 10)))
                .withSpecs(FieldSelector.ofPath("/inner/b"), ObjectSpecs.generator(Integer.class, Generators.sequence(100, 10)))
                .withSpecs(FieldSelector.ofPath("/intArray"), ObjectSpecs.generator(Integer.class, gen2).size(3, 5))
                .withSpecs(FieldSelector.ofPath("/inner/intArray"), ObjectSpecs.generator(Integer.class, gen2).size(3, 5))
                .withSpecs(FieldSelector.ofPath("/integerArray"), ObjectSpecs.generator(Integer.class, Generators.sequence(50, 2)).size(3, 5))
                .withSpecs(FieldSelector.ofPath("/inner/integerArray"), ObjectSpecs.generator(Integer.class, Generators.sequence(50, 2)).size(3, 5))
                .generate();

        // Test global sequence with key "key1"
        Assertions.assertEquals(10, obj.a);
        Assertions.assertEquals(15, obj.inner.a);

        // Test local sequences (unique per path)
        Assertions.assertEquals(100, obj.b);
        Assertions.assertEquals(100, obj.inner.b);

        // Test global sequence with key "key2" for intArray and inner.intArray
        Assertions.assertNotNull(obj.inner.intArray);
        Assertions.assertTrue(obj.inner.intArray.length >= 3 && obj.inner.intArray.length <= 5);
        for (int i = 0; i < obj.inner.intArray.length; i++) {
            Assertions.assertEquals(1000 + i * 100, obj.inner.intArray[i]);
        }

        Assertions.assertNotNull(obj.intArray);
        Assertions.assertTrue(obj.intArray.length >= 3 && obj.intArray.length <= 5);
        int expectedStart = 1000 + obj.inner.intArray.length * 100;
        for (int i = 0; i < obj.intArray.length; i++) {
            Assertions.assertEquals(expectedStart + i * 100, obj.intArray[i]);
        }

        // Test local sequences for integerArray and inner.integerArray
        Assertions.assertNotNull(obj.inner.integerArray);
        Assertions.assertTrue(obj.inner.integerArray.length >= 3 && obj.inner.integerArray.length <= 5);
        for (int i = 0; i < obj.inner.integerArray.length; i++) {
            Assertions.assertEquals(50 + i * 2, obj.inner.integerArray[i]);
        }

        Assertions.assertNotNull(obj.integerArray);
        Assertions.assertTrue(obj.integerArray.length >= 3 && obj.integerArray.length <= 5);
        for (int i = 0; i < obj.integerArray.length; i++) {
            Assertions.assertEquals(50 + i * 2, obj.integerArray[i]);
        }
    }


    private static class TestClass {
        private int a;
        private Integer b;
        private InnerClass inner;
        private int[] intArray;
        private Integer[] integerArray;
    }

    private static class InnerClass {
        private int a;
        private Integer b;
        private int[] intArray;
        private Integer[] integerArray;
    }
}
