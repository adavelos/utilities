package argonath.utils.test.random;

import argonath.random.EnumValueSelector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class TestEnumValueSelector {

    private enum TestEnum {
        A, B, C, D, E
    }

    @Test
    void testEnumValueSelectorWithReplacement() {
        EnumValueSelector<TestEnum> selector = new EnumValueSelector<>(true, TestEnum.class);
        Set<TestEnum> generatedValues = new HashSet<>();
        int iterations = 1000;

        for (int i = 0; i < iterations; i++) {
            TestEnum value = selector.drawValue(null);
            generatedValues.add(value);
            Assertions.assertNotNull(value, "Generated value should not be null");
        }

        Assertions.assertEquals(TestEnum.values().length, generatedValues.size(),
                "All enum values should be generated with enough iterations");
    }

    @Test
    void testEnumValueSelectorWithoutReplacement() {
        EnumValueSelector<TestEnum> selector = new EnumValueSelector<>(false, TestEnum.class);
        Set<TestEnum> generatedValues = new HashSet<>();
        int expectedSize = TestEnum.values().length;

        for (int i = 0; i < expectedSize; i++) {
            TestEnum value = selector.drawValue(null);
            Assertions.assertTrue(generatedValues.add(value),
                    "Each value should be unique when sampling without replacement");
        }

        Assertions.assertEquals(expectedSize, generatedValues.size(),
                "All enum values should be generated exactly once");

        // Test reset behavior
        TestEnum resetValue = selector.drawValue(null);
        Assertions.assertTrue(generatedValues.contains(resetValue),
                "After reset, values should start over");
    }

    @Test
    void testSingleValueEnum() {
        enum SingleValueEnum {SINGLE}
        EnumValueSelector<SingleValueEnum> selector = new EnumValueSelector<>(false, SingleValueEnum.class);

        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals(SingleValueEnum.SINGLE, selector.drawValue(null),
                    "Single value enum should always return the same value");
        }
    }
}