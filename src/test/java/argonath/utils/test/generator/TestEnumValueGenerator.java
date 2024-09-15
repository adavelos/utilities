package argonath.utils.test.generator;

import argonath.reflector.generator.FieldSelector;
import argonath.reflector.generator.ObjectGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static argonath.reflector.generator.Generators.enumValueSelector;
import static argonath.reflector.generator.model.ObjectSpecs.create;

public class TestEnumValueGenerator {

    private enum TestEnum1 {A, B, C}

    @Test
    public void testEnumValueSelectorWithReplacement() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/enum1"), create(TestEnum1.class).generator(enumValueSelector(TestEnum1.class, true)))
                .withSpecs(FieldSelector.ofPath("/enumArray1"), create(TestEnum1.class).generator(enumValueSelector(TestEnum1.class, true)).size(3))
                .generate();

        assertTestClassValues(obj, true);
    }

    @Test
    public void testEnumValueSelectorWithoutReplacement() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/enum1"), create(TestEnum1.class).generator(enumValueSelector(TestEnum1.class, false)))
                .withSpecs(FieldSelector.ofPath("/enumArray1"), create(TestEnum1.class).generator(enumValueSelector(TestEnum1.class, false)).size(3))
                .generate();

        assertTestClassValues(obj, false);
    }

    private void assertTestClassValues(TestClass obj, boolean withReplacement) {
        Assertions.assertNotNull(obj);
        Assertions.assertTrue(Set.of(TestEnum1.values()).contains(obj.enum1));


        Assertions.assertEquals(3, obj.enumArray1.size());
        obj.enumArray1.forEach(e -> Assertions.assertTrue(Set.of(TestEnum1.values()).contains(e)));

        if (!withReplacement) {
            // assert that all values are unique (size can vary from 1->3)
            Assertions.assertEquals(obj.enumArray1.size(), new HashSet<>(obj.enumArray1).size());
        }
    }

    private static class TestClass {
        private TestEnum1 enum1;
        private List<TestEnum1> enumArray1;
    }
}