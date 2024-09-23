package argonath.utils.test.generator;

import argonath.reflector.generator.FieldSelector;
import argonath.reflector.generator.ObjectGenerator;
import argonath.utils.test.generator.model.TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.temporal.ChronoUnit;

class TestObjectGeneratorSpecsTypes {

    @Test
    void testWithSpecsFile() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecsFile("test.model.class.specs")
                .generate();

        runAssertions(obj);
    }


    @Test
    void testSpecsString() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs(FieldSelector.ofPath("/intField"), "M|randomInt(5,10)")
                .withSpecs(FieldSelector.ofPath("/floatField"), "M|randomFloat(12.0,14.5)")
                .withSpecs(FieldSelector.ofPath("/doubleField"), "M|randomDouble(17.8,31.5)")
                .withSpecs(FieldSelector.ofPath("/longField"), "M|randomLong(400,2300)")
                .withSpecs(FieldSelector.ofPath("/charField"), "M|randomChar(c,t)")
                .withSpecs(FieldSelector.ofPath("/byteField"), "M|randomByte(-128,127)")
                .withSpecs(FieldSelector.ofPath("/shortField"), "M|randomShort(5,10)")
                .withSpecs(FieldSelector.ofPath("/booleanField"), "M|randomBoolean")
                .withSpecs(FieldSelector.ofPath("/stringField"), "M|randomString(an10)")
                .withSpecs(FieldSelector.ofPath("/bigIntegerField"), "M|randomBigInteger(500,1000)")
                .withSpecs(FieldSelector.ofPath("/bigDecimalField"), "M|randomBigDecimal(413.4,5139.4)")
                .withSpecs(FieldSelector.ofPath("/localDateField"), "M|randomLocalDate(-10,10)")
                .withSpecs(FieldSelector.ofPath("/localDateTimeField"), "M|randomLocalDateTime(-100,-50)")
                .withSpecs(FieldSelector.ofPath("/offsetDateTimeField"), "M|randomOffsetDateTime(50,150)")
                .withSpecs(FieldSelector.ofPath("/offsetTimeField"), "M|randomOffsetTime(-3,3)")
                .withSpecs(FieldSelector.ofPath("/zonedDateTimeField"), "M|randomZonedDateTime(0,50)")
                .withSpecs(FieldSelector.ofPath("/inner/innerIntField"), "M|randomInt(15,20)")
                .generate();

        runAssertions(obj);
    }

    @Test
    void testSpecsCompleteString() {
        TestClass obj = ObjectGenerator.create(TestClass.class)
                .withSpecs("/intField=M|randomInt(5,10)")
                .withSpecs("/floatField=M|randomFloat(12.0,14.5)")
                .withSpecs("/doubleField=M|randomDouble(17.8,31.5)")
                .withSpecs("/longField=M|randomLong(400,2300)")
                .withSpecs("/charField=M|randomChar(c,t)")
                .withSpecs("/byteField=M|randomByte(-128,127)")
                .withSpecs("/shortField=M|randomShort(5,10)")
                .withSpecs("/booleanField=M|randomBoolean")
                .withSpecs("/stringField=M|randomString(an10)")
                .withSpecs("/bigIntegerField=M|randomBigInteger(500,1000)")
                .withSpecs("/bigDecimalField=M|randomBigDecimal(413.4,5139.4)")
                .withSpecs("/localDateField=M|randomLocalDate(-10,10)")
                .withSpecs("/localDateTimeField=M|randomLocalDateTime(-100,-50)")
                .withSpecs("/offsetDateTimeField=M|randomOffsetDateTime(50,150)")
                .withSpecs("/offsetTimeField=M|randomOffsetTime(-3,3)")
                .withSpecs("/zonedDateTimeField=M|randomZonedDateTime(0,50)")
                .withSpecs("/inner/innerIntField=M|randomInt(15,20)")
                .generate();

        runAssertions(obj);
    }

    private static void runAssertions(TestClass obj) {
        Assertions.assertTrue(obj.intField >= 5 && obj.intField <= 10);
        Assertions.assertTrue(obj.floatField >= 12.0f && obj.floatField <= 14.5f);
        Assertions.assertTrue(obj.doubleField >= 17.8 && obj.doubleField <= 31.5);
        Assertions.assertTrue(obj.longField >= 400 && obj.longField <= 2300);
        Assertions.assertTrue(obj.charField >= 'c' && obj.charField <= 't');
        Assertions.assertTrue(obj.byteField >= Byte.MIN_VALUE && obj.byteField <= Byte.MAX_VALUE);
        Assertions.assertTrue(obj.shortField >= 5 && obj.shortField <= 10);
        Assertions.assertEquals(10, obj.stringField.length());
        Assertions.assertTrue(obj.stringField.matches("[a-zA-Z0-9]{10}"));

        Assertions.assertTrue(obj.bigIntegerField.compareTo(new BigInteger("500")) >= 0
                && obj.bigIntegerField.compareTo(new BigInteger("1000")) <= 0);
        Assertions.assertTrue(obj.bigDecimalField.compareTo(new BigDecimal("413.4")) >= 0
                && obj.bigDecimalField.compareTo(new BigDecimal("5139.4")) <= 0);

        LocalDate now = LocalDate.now();
        Assertions.assertTrue(obj.localDateField.isAfter(now.minusDays(11))
                && obj.localDateField.isBefore(now.plusDays(11)));

        LocalDateTime nowDateTime = LocalDateTime.now();
        Assertions.assertTrue(obj.localDateTimeField.isAfter(nowDateTime.minusDays(101))
                && obj.localDateTimeField.isBefore(nowDateTime.minusDays(49)));

        OffsetDateTime nowOffsetDateTime = OffsetDateTime.now();
        Assertions.assertTrue(obj.offsetDateTimeField.isAfter(nowOffsetDateTime.plusDays(49))
                && obj.offsetDateTimeField.isBefore(nowOffsetDateTime.plusDays(151)));

        OffsetTime nowOffsetTime = LocalDateTime.now().atOffset(ZoneOffset.UTC).toOffsetTime().truncatedTo(ChronoUnit.SECONDS);
        Assertions.assertTrue(obj.offsetTimeField.isAfter(nowOffsetTime.minusSeconds(4))
                && obj.offsetTimeField.isBefore(nowOffsetTime.plusSeconds(4)));

        ZonedDateTime nowZonedDateTime = ZonedDateTime.now();
        Assertions.assertTrue(obj.zonedDateTimeField.isAfter(nowZonedDateTime.minusSeconds(1))
                && obj.zonedDateTimeField.isBefore(nowZonedDateTime.plusDays(51)));
    }

}
