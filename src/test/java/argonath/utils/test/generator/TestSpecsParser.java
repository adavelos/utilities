package argonath.utils.test.generator;

import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.generator.model.Optionality;
import argonath.reflector.generator.model.SpecsExpressionParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.temporal.ChronoUnit;

class TestSpecsParser {

    private SpecsExpressionParser specsExpressionParser;

    @Test
    void testParseSpec() {
        String spec = "M|size(1, 3)|randomString(an10)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null);
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    void testParseSpecOptional() {
        String spec = "O|size(1, 3)|randomString(an10)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.OPTIONAL);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null);
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    void testParseSpecDifferentCardinality() {
        String spec = "M|size(2, 4)|randomString(an10)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 2);
        Assertions.assertEquals(result.cardinality().maxSize(), 4);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null);
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    void testParseSpecDifferentGenerator() {
        String spec = "M|size(1, 3)|randomInt(1, 10)";
        ObjectSpecs<Integer> result = SpecsExpressionParser.parseSpec("/path", spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
        Assertions.assertNotNull(result.generator());
        Integer intValue = result.generator().generate(null);
        Assertions.assertTrue(intValue >= 1 && intValue <= 10);
    }

    @Test
    void testRandomString() {
        String spec = "M|randomString(an10)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null);
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    void testRandomInt() {
        String spec = "M|randomInt(1, 10)";
        ObjectSpecs<Integer> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Integer intValue = result.generator().generate(null);
        Assertions.assertTrue(intValue >= 1 && intValue <= 10);
    }

    @Test
    void testParseCharRange() {
        String spec = "M|randomChar(a, z)";
        ObjectSpecs<Character> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Character charValue = result.generator().generate(null);
        Assertions.assertTrue(charValue >= 'a' && charValue <= 'z');
    }

    @Test
    void testRandomCharSingleRange() {
        String spec = "M|randomChar(f)";
        ObjectSpecs<Character> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Character charValue = result.generator().generate(null);
        Assertions.assertTrue(charValue >= 'A' && charValue <= 'f');
    }

    @Test
    void testRandomChar() {
        String spec = "M|randomChar()";
        ObjectSpecs<Character> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Character charValue = result.generator().generate(null);
        Assertions.assertTrue(charValue >= 'A' && charValue <= 'z');
    }

    // randomShort, randomByte, randomLong, randomDouble, randomFloat, randomBigInteger, randomBigDecimal, randomByteArray
    @Test
    void testRandomShort() {
        String spec = "M|randomShort(1, 10)";
        ObjectSpecs<Short> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Short shortValue = result.generator().generate(null);
        Assertions.assertTrue(shortValue >= 1 && shortValue <= 10);
    }

    @Test
    void testRandomByte() {
        String spec = "M|randomByte(1, 10)";
        ObjectSpecs<Byte> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Byte byteValue = result.generator().generate(null);
        Assertions.assertTrue(byteValue >= 1 && byteValue <= 10);
    }

    @Test
    void testRandomByteNoArgs() {
        String spec = "M|randomByte()";
        ObjectSpecs<Byte> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Byte byteValue = result.generator().generate(null);
        Assertions.assertTrue(byteValue >= Byte.MIN_VALUE && byteValue <= Byte.MAX_VALUE);
    }

    @Test
    void testRandomByteSingleArg() {
        String spec = "M|randomByte(10)";
        ObjectSpecs<Byte> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Byte byteValue = result.generator().generate(null);
        Assertions.assertTrue(byteValue >= Byte.MIN_VALUE && byteValue <= 10);
    }

    @Test
    void testRandomLong() {
        String spec = "M|randomLong(1, 10)";
        ObjectSpecs<Long> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Long longValue = result.generator().generate(null);
        Assertions.assertTrue(longValue >= 1 && longValue <= 10);
    }

    @Test
    void testRandomDouble() {
        String spec = "M|randomDouble(1, 10)";
        ObjectSpecs<Double> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Double doubleValue = result.generator().generate(null);
        Assertions.assertTrue(doubleValue >= 1 && doubleValue <= 10);
    }

    @Test
    void testRandomFloat() {
        String spec = "M|randomFloat(1, 10)";
        ObjectSpecs<Float> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Float floatValue = result.generator().generate(null);
        Assertions.assertTrue(floatValue >= 1 && floatValue <= 10);
    }

    @Test
    void testRandomBigInteger() {
        String spec = "M|randomBigInteger(1, 10)";
        ObjectSpecs<BigInteger> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        BigInteger bigIntegerValue = result.generator().generate(null);
        Assertions.assertTrue(bigIntegerValue.longValue() >= 1 && bigIntegerValue.longValue() <= 10);
    }

    @Test
    void testRandomBigDecimal() {
        String spec = "M|randomBigDecimal(1, 10)";
        ObjectSpecs<BigDecimal> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        BigDecimal bigDecimalValue = result.generator().generate(null);
        Assertions.assertTrue(bigDecimalValue.doubleValue() >= 1 && bigDecimalValue.doubleValue() <= 10);
    }

    @Test
    void testRandomByteArray() {
        String spec = "M|randomByteArray(100, 200)";
        ObjectSpecs<byte[]> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        byte[] byteArrayValue = result.generator().generate(null);
        Assertions.assertTrue(byteArrayValue.length >= 100 && byteArrayValue.length <= 200);
    }

    @Test
    void testRandomLocalDate() {
        String spec = "M|randomLocalDate(-10, 10)";
        ObjectSpecs<LocalDate> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDate dateValue = result.generator().generate(null);
        Assertions.assertTrue(dateValue.isAfter(LocalDate.now().minusDays(11)) && dateValue.isBefore(LocalDate.now().plusDays(11)));
    }

    @Test
    void testRandomLocalDateTime() {
        String spec = "M|randomLocalDateTime(-100, -50)";
        ObjectSpecs<LocalDateTime> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDateTime dateTimeValue = result.generator().generate(null);
        Assertions.assertTrue(dateTimeValue.isAfter(LocalDateTime.now().minusDays(101)) && dateTimeValue.isBefore(LocalDateTime.now().minusDays(-49)));
    }

    @Test
    void testRandomOffsetDateTime() {
        String spec = "M|randomOffsetDateTime(0, 10)";
        ObjectSpecs<OffsetDateTime> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        OffsetDateTime offsetDateTimeValue = result.generator().generate(null);
        Assertions.assertTrue(offsetDateTimeValue.isAfter(OffsetDateTime.now().minusDays(1)) && offsetDateTimeValue.isBefore(OffsetDateTime.now().plusDays(11)));
    }

    @Test
    void testRandomOffsetTime() {
        String spec = "M|randomOffsetTime(0, 10)";
        ObjectSpecs<OffsetTime> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        OffsetTime offsetTimeValue = result.generator().generate(null);
        OffsetTime offsetTime1 = LocalDateTime.now().atOffset(ZoneOffset.UTC).toOffsetTime().truncatedTo(ChronoUnit.SECONDS);
        OffsetTime offsetTime2 = LocalDateTime.now().atOffset(ZoneOffset.UTC).toOffsetTime().truncatedTo(ChronoUnit.SECONDS);
        Assertions.assertTrue(offsetTimeValue.isAfter(offsetTime1.minusSeconds(1)) && offsetTimeValue.isBefore(offsetTime2.plusSeconds(11)));
    }

    @Test
    void testRandomZonedDateTime() {
        String spec = "M|randomZonedDateTime(0, 10)";
        ObjectSpecs<ZonedDateTime> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        ZonedDateTime zonedDateTimeValue = result.generator().generate(null);
        Assertions.assertTrue(zonedDateTimeValue.isAfter(ZonedDateTime.now()) && zonedDateTimeValue.isBefore(ZonedDateTime.now().plusDays(11)));
    }

    @Test
    void testRandomBoolean() {
        String spec = "M|randomBoolean()";
        ObjectSpecs<Boolean> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Boolean booleanValue = result.generator().generate(null);
        Assertions.assertTrue(booleanValue == true || booleanValue == false);
    }

    @Test
    void testNow() {
        String spec = "M|now()";
        ObjectSpecs<LocalDateTime> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDateTime nowValue = result.generator().generate(null);
        Assertions.assertTrue(nowValue.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void testToday() {
        String spec = "M|today()";
        ObjectSpecs<LocalDate> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDate todayValue = result.generator().generate(null);
        Assertions.assertEquals(todayValue, LocalDate.now());
    }

    @Test
    void testLoremIpsum() {
        String spec = "M|loremIpsum(100)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String loremIpsumValue = result.generator().generate(null);
        Assertions.assertEquals(100, loremIpsumValue.length());
    }

    @Test
    void testLoremIpsumRange() {
        String spec = "M|loremIpsum(100, 500)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String loremIpsumValue = result.generator().generate(null);
        Assertions.assertTrue(loremIpsumValue.length() >= 100 && loremIpsumValue.length() <= 500);
    }

    @Test
    void testSequence() {
        String spec = "M|sequence(1, 1)";
        ObjectSpecs<Integer> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Integer sequenceValue = result.generator().generate(null);
        Assertions.assertEquals(sequenceValue, 1);
    }

    @Test
    void testValueSelectorWithReplacement() {
        String spec = "M|valueSelector(true,A,B,C)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String valueSelectorValue = result.generator().generate(null);
        Assertions.assertTrue(valueSelectorValue.equals("A") || valueSelectorValue.equals("B") || valueSelectorValue.equals("C"));
    }

    @Test
    void testValueSelectorWithoutReplacement() {
        String spec = "M|valueSelector(false,A,B,C)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String valueSelectorValue = result.generator().generate(null);
        Assertions.assertTrue(valueSelectorValue.equals("A") || valueSelectorValue.equals("B") || valueSelectorValue.equals("C"));
    }

    @Test
    void testEnumSelector() {
        String spec = "M|enumSelector(argonath.utils.test.generator.TestSpecsParser$MyEnum, false)";
        ObjectSpecs<MyEnum> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        MyEnum enumSelectorValue = result.generator().generate(null);
        Assertions.assertTrue(enumSelectorValue instanceof MyEnum);
    }

    @Test
    void testSize() {
        String spec = "M|size(1, 3)";
        ObjectSpecs<String> result = SpecsExpressionParser.parseSpec("/path", spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
    }

    enum MyEnum {
        A, B, C
    }

}