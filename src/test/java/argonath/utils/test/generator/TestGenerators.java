package argonath.utils.test.generator;

import argonath.reflector.config.Configuration;
import argonath.reflector.generator.Generators;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.temporal.ChronoUnit;

class TestGenerators {
    @BeforeAll
    static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    void testRandomString() {
        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("an10").generate(null);
            Assertions.assertTrue(randomString.matches("[a-zA-Z0-9]{10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("an..10").generate(null);
            Assertions.assertTrue(randomString.matches("[a-zA-Z0-9]{1,10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("an5..10").generate(null);
            Assertions.assertTrue(randomString.matches("[a-zA-Z0-9]{5,10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("a10").generate(null);
            Assertions.assertTrue(randomString.matches("[a-zA-Z]{10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("a..10").generate(null);
            Assertions.assertTrue(randomString.matches("[a-zA-Z]{1,10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("a5..10").generate(null);
            Assertions.assertTrue(randomString.matches("[a-zA-Z]{5,10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("n10").generate(null);
            Assertions.assertTrue(randomString.matches("[0-9]{10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("n..10").generate(null);
            Assertions.assertTrue(randomString.matches("[0-9]{1,10}"));
        }

        for (int i = 0; i < 100; i++) {
            String randomString = Generators.randomString("n5..10").generate(null);
            Assertions.assertTrue(randomString.matches("[0-9]{5,10}"));
        }
    }

    @Test
    void testRandomInt() {
        for (int i = 0; i < 100; i++) {
            int randomInt = Generators.randomInt(-100, 100).generate(null);
            Assertions.assertTrue(randomInt >= -100 && randomInt <= 100);
        }
    }

    @Test
    void testRandomShort() {
        for (int i = 0; i < 100; i++) {
            int randomShort = Generators.randomShort((short) -100, (short) 100).generate(null);
            Assertions.assertTrue(randomShort >= -100 && randomShort <= 100);
        }
    }

    @Test
    void testRandomByte() {
        for (int i = 0; i < 100; i++) {
            int randomByte = Generators.randomByte((byte) -100, (byte) 100).generate(null);
            Assertions.assertTrue(randomByte >= -100 && randomByte <= 100);
        }
    }

    @Test
    void testRandomCharacter() {
        for (int i = 0; i < 100; i++) {
            int randomCharacter = Generators.randomCharacter((char) 0, (char) 100).generate(null);
            Assertions.assertTrue(randomCharacter >= 0 && randomCharacter <= 100);
        }
    }

    @Test
    void testRandomLong() {
        for (int i = 0; i < 100; i++) {
            long randomLong = Generators.randomLong(-100, 100).generate(null);
            Assertions.assertTrue(randomLong >= -100 && randomLong <= 100);
        }
    }

    @Test
    void testRandomDouble() {
        for (int i = 0; i < 100; i++) {
            double randomDouble = Generators.randomDouble(-100, 100).generate(null);
            Assertions.assertTrue(randomDouble >= -100 && randomDouble <= 100);
        }
    }

    @Test
    void testRandomFloat() {
        for (int i = 0; i < 100; i++) {
            float randomFloat = Generators.randomFloat(-100, 100).generate(null);
            Assertions.assertTrue(randomFloat >= -100 && randomFloat <= 100);
        }
    }

    @Test
    void testRandomBigInteger() {
        for (int i = 0; i < 100; i++) {
            BigInteger randomBigInteger = Generators.randomBigInteger(-100, 100).generate(null);
            Assertions.assertTrue(randomBigInteger.compareTo(BigInteger.valueOf(-100)) >= 0 && randomBigInteger.compareTo(BigInteger.valueOf(100)) <= 0);
        }
    }

    @Test
    void testRandomBigDecimal() {
        for (int i = 0; i < 100; i++) {
            BigDecimal randomBigDecimal = Generators.randomBigDecimal(-100, 100).generate(null);
            Assertions.assertTrue(randomBigDecimal.compareTo(BigDecimal.valueOf(-100)) >= 0 && randomBigDecimal.compareTo(BigDecimal.valueOf(100)) <= 0);
        }
    }


    @Test
    void testRandomLocalDateTime() {
        for (int i = 0; i < 100; i++) {
            LocalDateTime dateTime = Generators.randomLocalDateTime(-100, 100).generate(null);
            Assertions.assertTrue(dateTime.isAfter(LocalDateTime.now().minusDays(100)) && dateTime.isBefore(LocalDateTime.now().plusDays(100)));
        }
    }

    @Test
    void testRandomOffsetDateTime() {
        for (int i = 0; i < 100; i++) {
            OffsetDateTime dateTime = Generators.randomOffsetDateTime(-100, 100).generate(null);
            OffsetDateTime offsetDateTime1 = LocalDateTime.now().atOffset(ZoneOffset.UTC).minusDays(101).truncatedTo(ChronoUnit.SECONDS);
            OffsetDateTime offsetDateTime2 = LocalDateTime.now().atOffset(ZoneOffset.UTC).plusDays(101).truncatedTo(ChronoUnit.SECONDS);
            Assertions.assertTrue(dateTime.isAfter(offsetDateTime1) && dateTime.isBefore(offsetDateTime2));
        }
    }

    @Test
    void testRandomOffsetTime() {
        for (int i = 0; i < 100; i++) {
            OffsetTime time = Generators.randomOffsetTime(-100, 100).generate(null);
            OffsetTime offsetTime1 = LocalDateTime.now().atOffset(ZoneOffset.UTC).toOffsetTime().truncatedTo(ChronoUnit.SECONDS);
            OffsetTime offsetTime2 = LocalDateTime.now().atOffset(ZoneOffset.UTC).toOffsetTime().truncatedTo(ChronoUnit.SECONDS);
            Assertions.assertTrue(time.isAfter(offsetTime1.minusSeconds(101)) && time.isBefore(offsetTime2.plusSeconds(101)));
        }
    }

    @Test
    void testRandomZonedDateTime() {
        for (int i = 0; i < 100; i++) {
            ZonedDateTime dateTime = Generators.randomZonedDateTime(-100, 100).generate(null);
            Assertions.assertTrue(dateTime.isAfter(ZonedDateTime.now().minusDays(100)) && dateTime.isBefore(ZonedDateTime.now().plusDays(100)));
        }
    }

    @Test
    void testRandomBoolean() {
        for (int i = 0; i < 100; i++) {
            Boolean randomBool = Generators.randomBoolean().generate(null);
            Assertions.assertTrue(randomBool != null);
        }
    }

    @Test
    void testRandomByteArray() {
        for (int i = 0; i < 100; i++) {
            byte[] randomByteArray = Generators.randomByteArray(0, 100).generate(null);
            Assertions.assertTrue(randomByteArray.length >= 0 && randomByteArray.length <= 100);
        }
    }
}
