package argonath.utils.test.generator;

import argonath.reflector.generator.FieldSelector;
import argonath.reflector.generator.ObjectGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static argonath.reflector.generator.Generators.valueSelector;
import static argonath.reflector.generator.model.ObjectSpecs.create;

public class TestCodeListGenerator {

    @Test
    public void testCodeListGeneratorWithReplacement() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/a"), create(Integer.class).generator(valueSelector(true, Set.of(1, 2, 3))))
                .withSpecs(FieldSelector.ofPath("/b"), create(Long.class).generator(valueSelector(true, Set.of(100L, 200L, 300L))))
                .withSpecs(FieldSelector.ofPath("/c"), create(String.class).generator(valueSelector(true, Set.of("X", "Y", "Z"))))
                .withSpecs(FieldSelector.ofPath("/intArray"), create(Integer.class).generator(valueSelector(true, Set.of(10, 20, 30))).size(3, 3))
                .withSpecs(FieldSelector.ofPath("/longArray"), create(Long.class).generator(valueSelector(true, Set.of(1000L, 2000L, 3000L))).size(3, 3))
                .withSpecs(FieldSelector.ofPath("/stringArray"), create(String.class).generator(valueSelector(true, Set.of("A", "B", "C"))).size(3, 3))
                .generate();

        assertTestClassValues(obj, true);
    }

    @Test
    public void testCodeListGeneratorWithoutReplacement() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/a"), create(Integer.class).generator(valueSelector(false, Set.of(1, 2, 3))))
                .withSpecs(FieldSelector.ofPath("/b"), create(Long.class).generator(valueSelector(false, Set.of(100L, 200L, 300L))))
                .withSpecs(FieldSelector.ofPath("/c"), create(String.class).generator(valueSelector(false, Set.of("X", "Y", "Z"))))
                .withSpecs(FieldSelector.ofPath("/intArray"), create(Integer.class).generator(valueSelector(false, Set.of(10, 20, 30))).size(3, 3))
                .withSpecs(FieldSelector.ofPath("/longArray"), create(Long.class).generator(valueSelector(false, Set.of(1000L, 2000L, 3000L))).size(3, 3))
                .withSpecs(FieldSelector.ofPath("/stringArray"), create(String.class).generator(valueSelector(false, Set.of("A", "B", "C"))).size(3, 3))
                .generate();

        assertTestClassValues(obj, false);
    }

    private void assertTestClassValues(TestClass obj, boolean withReplacement) {
        Assertions.assertNotNull(obj);
        Assertions.assertTrue(Set.of(1, 2, 3).contains(obj.a));
        Assertions.assertTrue(Set.of(100L, 200L, 300L).contains(obj.b));
        Assertions.assertTrue(Set.of("X", "Y", "Z").contains(obj.c));

        Assertions.assertEquals(3, obj.intArray.size());
        obj.intArray.forEach(i -> Assertions.assertTrue(Set.of(10, 20, 30).contains(i)));

        Assertions.assertEquals(3, obj.longArray.size());
        obj.longArray.forEach(l -> Assertions.assertTrue(Set.of(1000L, 2000L, 3000L).contains(l)));

        Assertions.assertEquals(3, obj.stringArray.size());
        obj.stringArray.forEach(s -> Assertions.assertTrue(Set.of("A", "B", "C").contains(s)));

        Assertions.assertEquals(3, obj.intArray.size());
        Assertions.assertEquals(3, obj.longArray.size());
        Assertions.assertEquals(3, obj.stringArray.size());

        if (!withReplacement) {
            // assert that all values are unique (size can vary from 1->3)
            Assertions.assertEquals(obj.intArray.size(), new HashSet<>(obj.intArray).size());
            Assertions.assertEquals(obj.longArray.size(), new HashSet<>(obj.intArray).size());
            Assertions.assertEquals(obj.stringArray.size(), new HashSet<>(obj.intArray).size());
        }

    }

    private static class TestClass {
        private Integer a;
        private Long b;
        private String c;
        private List<Integer> intArray;
        private List<Long> longArray;
        private List<String> stringArray;
    }
}