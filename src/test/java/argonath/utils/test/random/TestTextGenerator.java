package argonath.utils.test.random;

import argonath.random.TextGenerator;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestTextGenerator {

    @Test
    void testLoremIpsumZeroLength() {
        String result = TextGenerator.loremIpsum(0);
        assertEquals("", result, "Zero length should return an empty string");
    }

    @RepeatedTest(10)
    void testLoremIpsumSingleCharacter() {
        String result = TextGenerator.loremIpsum(1);
        assertEquals(1, result.length(), "Single character length should be respected");
        assertTrue(result.matches("[a-z]"), "Single character should be a lowercase letter");
    }

    @Test
    void testLoremIpsumFixedLength() {
        int expectedLength = 100;
        String result = TextGenerator.loremIpsum(expectedLength);

        assertTrue(result.length() >= expectedLength, "Generated text should have at least the specified length");
        assertTrue(result.trim().length() > 0, "Generated text should not be empty");
        assertTrue(result.matches("^[a-zA-Z ]+$"), "Generated text should only contain letters and spaces");
    }

    @RepeatedTest(10)
    void testLoremIpsumRandomLength() {
        int minLength = 50;
        int maxLength = 150;
        String result = TextGenerator.loremIpsum(minLength, maxLength);

        assertTrue(result.length() >= minLength && result.length() <= maxLength + 1,
                "Generated text length should be within or slightly exceeding the specified range");
        assertTrue(result.trim().length() > 0, "Generated text should not be empty");
        assertTrue(result.matches("^[a-zA-Z ]+$"), "Generated text should only contain letters and spaces");
    }

    @Test
    void testLoremIpsumVeryShortLength() {
        int length = 5;
        String result = TextGenerator.loremIpsum(length);
        assertTrue(result.length() >= length, "Very short length should be respected or slightly exceeded");
    }

    @Test
    void testLoremIpsumLongLength() {
        int length = 10000;
        String result = TextGenerator.loremIpsum(length);
        assertTrue(result.length() == length, "Long length should be respected");
    }

    @Test
    void testLoremIpsumMinMaxEqual() {
        int length = 100;
        String result = TextGenerator.loremIpsum(length, length);
        assertTrue(result.length() >= length, "When min and max are equal, the length should be at least the specified length");
    }

    @Test
    void testLoremIpsumInvalidRange() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TextGenerator.loremIpsum(100, 50);
        });

        String expectedMessage = "Invalid Argument: Min length is greater than Max length";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testLoremIpsumNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            TextGenerator.loremIpsum(-10);
        }, "Should throw IllegalArgumentException for negative length");
    }

    @Test
    void testConsistencyOfWordBank() {
        String result1 = TextGenerator.loremIpsum(1000);
        String result2 = TextGenerator.loremIpsum(1000);
        assertNotEquals(result1, result2, "Two generated texts should not be identical");
    }
}