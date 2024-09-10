package argonath.utils.test.generator;

import argonath.reflector.generator.ObjectGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;

public class TestObjectGeneratorSimpleTypes {
    /*
        - Selectors variances
        - with Specs
        - with Value
        - Code List
        - SEED
        - All Mandatory
     */

    @Test
    public void testWithSpecsWithAllMandatory() {

        for (int i = 0; i < 100; i++) {
            Outer obj = ObjectGenerator.create(Outer.class)
                    .withAllMandatory()
                    .generate();

            Assertions.assertNotNull(obj);
            Assertions.assertNotNull(obj.bigIntegerField);
            Assertions.assertTrue(obj.bigIntegerField.compareTo(BigInteger.ONE) >= 0 && obj.bigIntegerField.compareTo(BigInteger.valueOf(101)) <= 0);

            Assertions.assertNotNull(obj.bigDecimalField);
            Assertions.assertTrue(obj.bigDecimalField.compareTo(BigDecimal.ONE) >= 0 && obj.bigDecimalField.compareTo(BigDecimal.valueOf(101)) <= 0);

            Assertions.assertNotNull(obj.localDateField);
            Assertions.assertTrue(obj.localDateField.isAfter(LocalDate.now().minusDays(101)) && obj.localDateField.isBefore(LocalDate.now().plusDays(101)));

            Assertions.assertNotNull(obj.localDateTimeField);
            Assertions.assertTrue(obj.localDateTimeField.isAfter(LocalDateTime.now().minusDays(101)) && obj.localDateTimeField.isBefore(LocalDateTime.now().plusDays(101)));

            Assertions.assertNotNull(obj.offsetDateTimeField);
            Assertions.assertTrue(obj.offsetDateTimeField.isAfter(OffsetDateTime.now().minusDays(101)) && obj.offsetDateTimeField.isBefore(OffsetDateTime.now().plusDays(101)));

            Assertions.assertNotNull(obj.offsetTimeField);
            LocalTime now = LocalTime.now();
            Assertions.assertTrue(obj.offsetTimeField.isAfter(OffsetTime.of(now.minusSeconds(101), ZoneOffset.UTC)) && obj.offsetTimeField.isBefore(OffsetTime.of(now.plusSeconds(101), ZoneOffset.UTC)));

            Assertions.assertNotNull(obj.zonedDateTimeField);
            Assertions.assertTrue(obj.zonedDateTimeField.isAfter(ZonedDateTime.now().minusDays(101)) && obj.zonedDateTimeField.isBefore(ZonedDateTime.now().plusDays(101)));

            // assertions for inner object
            Inner inner = obj.inner;
            Assertions.assertNotNull(inner);

            Assertions.assertNotNull(inner.innerBigIntegerField);
            Assertions.assertTrue(inner.innerBigIntegerField.compareTo(BigInteger.ONE) >= 0 && inner.innerBigIntegerField.compareTo(BigInteger.valueOf(101)) <= 0);

            Assertions.assertNotNull(inner.innerBigDecimalField);
            Assertions.assertTrue(inner.innerBigDecimalField.compareTo(BigDecimal.ONE) >= 0 && inner.innerBigDecimalField.compareTo(BigDecimal.valueOf(101)) <= 0);

            Assertions.assertNotNull(inner.innerLocalDateField);
            Assertions.assertTrue(inner.innerLocalDateField.isAfter(LocalDate.now().minusDays(101)) && inner.innerLocalDateField.isBefore(LocalDate.now().plusDays(101)));

            Assertions.assertNotNull(inner.innerLocalDateTimeField);
            Assertions.assertTrue(inner.innerLocalDateTimeField.isAfter(LocalDateTime.now().minusDays(101)) && inner.innerLocalDateTimeField.isBefore(LocalDateTime.now().plusDays(101)));

            Assertions.assertNotNull(inner.innerOffsetDateTimeField);
            Assertions.assertTrue(inner.innerOffsetDateTimeField.isAfter(OffsetDateTime.now().minusDays(101)) && inner.innerOffsetDateTimeField.isBefore(OffsetDateTime.now().plusDays(101)));

            Assertions.assertNotNull(inner.innerOffsetTimeField);
            Assertions.assertTrue(inner.innerOffsetTimeField.isAfter(OffsetTime.of(now.minusSeconds(101), ZoneOffset.UTC)) && inner.innerOffsetTimeField.isBefore(OffsetTime.of(now.plusSeconds(101), ZoneOffset.UTC)));

            Assertions.assertNotNull(inner.innerZonedDateTimeField);
            Assertions.assertTrue(inner.innerZonedDateTimeField.isAfter(ZonedDateTime.now().minusDays(101)) && inner.innerZonedDateTimeField.isBefore(ZonedDateTime.now().plusDays(101)));
        }
    }

    private static class Outer {
        BigInteger bigIntegerField;
        BigDecimal bigDecimalField;
        LocalDate localDateField;
        LocalDateTime localDateTimeField;
        OffsetDateTime offsetDateTimeField;
        OffsetTime offsetTimeField;
        ZonedDateTime zonedDateTimeField;

        Inner inner;
    }

    private static class Inner {
        BigInteger innerBigIntegerField;
        BigDecimal innerBigDecimalField;
        LocalDate innerLocalDateField;
        LocalDateTime innerLocalDateTimeField;
        OffsetDateTime innerOffsetDateTimeField;
        OffsetTime innerOffsetTimeField;
        ZonedDateTime innerZonedDateTimeField;
    }

}