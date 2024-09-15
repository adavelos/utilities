package argonath.utils.test.random;

import argonath.random.ValueSelector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class TestValueSelector {

    @Test
    public void testValueSelectorWithReplacement() {
        ValueSelector<Integer> selector = new ValueSelector<>(true, Set.of(1, 2, 3, 4, 5));
        Set<Integer> generatedValues = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Integer value = selector.drawValue(null);
            Assertions.assertTrue(value >= 1 && value <= 5, "Generated value should be between 1 and 5");
            generatedValues.add(value);
        }
        Assertions.assertEquals(5, generatedValues.size(), "All values should be generated with replacement");

        // Test that values are indeed replaced (i.e., can be selected multiple times)
        ValueSelector<String> singleValueSelector = new ValueSelector<>(true, Set.of("A"));
        for (int i = 0; i < 10; i++) {
            Assertions.assertEquals("A", singleValueSelector.drawValue(null), "Single value should be repeatedly selected");
        }
    }

    @Test
    public void testValueSelectorWithoutReplacement() {
        ValueSelector<String> selector = new ValueSelector<>(false, Set.of("A", "B", "C"));
        Set<String> uniqueValues = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            String value = selector.drawValue(null);
            Assertions.assertTrue(uniqueValues.add(value), "Each value should be unique without replacement");
        }
        Assertions.assertEquals(3, uniqueValues.size(), "All values should be generated without replacement");

        // Test reset behavior
        String resetValue = selector.drawValue(null);
        Assertions.assertTrue(uniqueValues.contains(resetValue), "After reset, values should start over");

        // Test that all values are generated before reset
        ValueSelector<Integer> largerSelector = new ValueSelector<>(false, Set.of(1, 2, 3, 4, 5));
        Set<Integer> generatedValues = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            generatedValues.add(largerSelector.drawValue(null));
        }
        Assertions.assertEquals(5, generatedValues.size(), "All values should be generated before reset");
    }

    @Test
    public void testCommonBehaviors() {
        // Test empty set constructor
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new ValueSelector<>(true, new HashSet<>());
        }, "Constructor should throw IllegalArgumentException for empty set");

        // Test single value selector
        ValueSelector<Boolean> singleValueSelector = new ValueSelector<>(false, Set.of(true));
        for (int i = 0; i < 10; i++) {
            Assertions.assertTrue(singleValueSelector.drawValue(null), "Single value selector should always return true");
        }

        // Test large set of values
        Set<Integer> largeSet = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            largeSet.add(i);
        }
        ValueSelector<Integer> largeSelector = new ValueSelector<>(true, largeSet);
        Set<Integer> generatedValues = new HashSet<>();
        for (int i = 0; i < 100000; i++) {
            generatedValues.add(largeSelector.drawValue(null));
        }
        Assertions.assertTrue(generatedValues.size() > 9000, "Most values should be generated from a large set");
    }
}