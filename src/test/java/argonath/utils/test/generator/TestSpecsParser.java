package argonath.utils.test.generator;

import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.generator.model.Optionality;
import argonath.reflector.generator.model.SpecsFileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

public class TestSpecsParser {

    private SpecsFileLoader specsFileLoader;

    @Test
    public void testParseSpec() {
        String spec = "M|size(1, 3)|randomString(an10)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null, "an10");
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    public void testParseSpecOptional() {
        String spec = "O|size(1, 3)|randomString(an10)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.OPTIONAL);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null, "an10");
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    public void testParseSpecDifferentCardinality() {
        String spec = "M|size(2, 4)|randomString(an10)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 2);
        Assertions.assertEquals(result.cardinality().maxSize(), 4);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null, "an10");
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    public void testParseSpecDifferentGenerator() {
        String spec = "M|size(1, 3)|randomInt(1, 10)";
        ObjectSpecs<Integer> result = SpecsFileLoader.parseSpec(spec);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertEquals(result.cardinality().minSize(), 1);
        Assertions.assertEquals(result.cardinality().maxSize(), 3);
        Assertions.assertNotNull(result.generator());
        Integer intValue = result.generator().generate(null, "1,10");
        Assertions.assertTrue(intValue >= 1 && intValue <= 10);
    }

    @Test
    public void testRandomString() {
        String spec = "M|randomString(an10)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String stringValue = result.generator().generate(null, "10");
        Assertions.assertEquals(stringValue.length(), 10);
    }

    @Test
    public void testRandomInt() {
        String spec = "M|randomInt(1, 10)";
        ObjectSpecs<Integer> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Integer intValue = result.generator().generate(null, "1,10");
        Assertions.assertTrue(intValue >= 1 && intValue <= 10);
    }

    @Test
    public void testRandomLocalDate() {
        String spec = "M|randomLocalDate(-10, 10)";
        ObjectSpecs<LocalDate> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDate dateValue = result.generator().generate(null, "-10,10");
        Assertions.assertTrue(dateValue.isAfter(LocalDate.now().minusDays(10)) && dateValue.isBefore(LocalDate.now().plusDays(10)));
    }

    @Test
    public void testRandomLocalDateTime() {
        String spec = "M|randomLocalDateTime(-100, -50)";
        ObjectSpecs<LocalDateTime> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDateTime dateTimeValue = result.generator().generate(null, "-100,-50");
        Assertions.assertTrue(dateTimeValue.isAfter(LocalDateTime.now().minusDays(100)) && dateTimeValue.isBefore(LocalDateTime.now().minusDays(50)));
    }

    @Test
    public void testRandomOffsetDateTime() {
        String spec = "M|randomOffsetDateTime(0, 10)";
        ObjectSpecs<OffsetDateTime> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        OffsetDateTime offsetDateTimeValue = result.generator().generate(null, "0,10");
        Assertions.assertTrue(offsetDateTimeValue.isAfter(OffsetDateTime.now()) && offsetDateTimeValue.isBefore(OffsetDateTime.now().plusDays(10)));
    }

    @Test
    public void testRandomOffsetTime() {
        String spec = "M|randomOffsetTime(0, 10)";
        ObjectSpecs<OffsetTime> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        OffsetTime offsetTimeValue = result.generator().generate(null, "0,10");
       // Assertions.assertTrue(offsetTimeValue.isAfter(OffsetTime.now()) && offsetTimeValue.isBefore(OffsetTime.now().plusHours(10)));
    }

    @Test
    public void testRandomZonedDateTime() {
        String spec = "M|randomZonedDateTime(0, 10)";
        ObjectSpecs<ZonedDateTime> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        ZonedDateTime zonedDateTimeValue = result.generator().generate(null, "0,10");
        Assertions.assertTrue(zonedDateTimeValue.isAfter(ZonedDateTime.now()) && zonedDateTimeValue.isBefore(ZonedDateTime.now().plusDays(10)));
    }

    @Test
    public void testRandomBoolean() {
        String spec = "M|randomBoolean()";
        ObjectSpecs<Boolean> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Boolean booleanValue = result.generator().generate(null, "");
        Assertions.assertTrue(booleanValue == true || booleanValue == false);
    }

    @Test
    public void testNow() {
        String spec = "M|now()";
        ObjectSpecs<LocalDateTime> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDateTime nowValue = result.generator().generate(null, "");
        Assertions.assertTrue(nowValue.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    public void testToday() {
        String spec = "M|today()";
        ObjectSpecs<LocalDate> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        LocalDate todayValue = result.generator().generate(null, "");
        Assertions.assertEquals(todayValue, LocalDate.now());
    }

    @Test
    public void testLoremIpsum() {
        String spec = "M|loremIpsum(100)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String loremIpsumValue = result.generator().generate(null, "100");
        Assertions.assertEquals(100, loremIpsumValue.length());
    }

    @Test
    public void testLoremIpsumRange() {
        String spec = "M|loremIpsum(100, 500)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String loremIpsumValue = result.generator().generate(null, "100,500");
        Assertions.assertTrue(loremIpsumValue.length() >= 100 && loremIpsumValue.length() <= 500);
    }

    @Test
    public void testSequence() {
        String spec = "M|sequence(1, 1)";
        ObjectSpecs<Integer> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        Integer sequenceValue = result.generator().generate(null, "1,1");
        Assertions.assertEquals(sequenceValue, 1);
    }

    @Test
    public void testValueSelectorWithReplacement() {
        String spec = "M|valueSelector(true,A,B,C)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String valueSelectorValue = result.generator().generate(null, "true,A,B,C");
        Assertions.assertTrue(valueSelectorValue.equals("A") || valueSelectorValue.equals("B") || valueSelectorValue.equals("C"));
    }

    @Test
    public void testValueSelectorWithoutReplacement() {
        String spec = "M|valueSelector(false,A,B,C)";
        ObjectSpecs<String> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        String valueSelectorValue = result.generator().generate(null, "false,A,B,C");
        Assertions.assertTrue(valueSelectorValue.equals("A") || valueSelectorValue.equals("B") || valueSelectorValue.equals("C"));
    }

    @Test
    public void testEnumSelector() {
        String spec = "M|enumSelector(argonath.utils.test.generator.TestSpecsParser$MyEnum, false)";
        ObjectSpecs<MyEnum> result = SpecsFileLoader.parseSpec(spec);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.optionality(), Optionality.MANDATORY);
        Assertions.assertNotNull(result.generator());
        MyEnum enumSelectorValue = result.generator().generate(null, "argonath.utils.test.generator.TestSpecsParser$MyEnum,false");
        Assertions.assertTrue(enumSelectorValue instanceof MyEnum);
    }

    public enum MyEnum {
        A, B, C
    }

}