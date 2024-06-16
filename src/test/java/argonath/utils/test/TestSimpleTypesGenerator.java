package argonath.utils.test;

import argonath.utils.Assert;
import argonath.utils.reflector.generator.SimpleTypes;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;

public class TestSimpleTypesGenerator {

    @Test
    public void testDefaultGenerator() {
        String defaultString = SimpleTypes.generateDefaultValue(String.class);
        Assert.isTrue(defaultString != null, "Default String is null");

        Boolean defaultBoolean = SimpleTypes.generateDefaultValue(Boolean.class);
        Assert.isTrue(defaultBoolean != null, "Default Boolean is null");

        Character defaultCharacter = SimpleTypes.generateDefaultValue(Character.class);
        Assert.isTrue(defaultCharacter != null, "Default Character is null");

        Byte defaultByte = SimpleTypes.generateDefaultValue(Byte.class);
        Assert.isTrue(defaultByte != null, "Default Byte is null");

        Short defaultShort = SimpleTypes.generateDefaultValue(Short.class);
        Assert.isTrue(defaultShort != null, "Default Short is null");

        Integer defaultInteger = SimpleTypes.generateDefaultValue(Integer.class);
        Assert.isTrue(defaultInteger != null, "Default Integer is null");

        Long defaultLong = SimpleTypes.generateDefaultValue(Long.class);
        Assert.isTrue(defaultLong != null, "Default Long is null");

        Float defaultFloat = SimpleTypes.generateDefaultValue(Float.class);
        Assert.isTrue(defaultFloat != null, "Default Float is null");

        Double defaultDouble = SimpleTypes.generateDefaultValue(Double.class);
        Assert.isTrue(defaultDouble != null, "Default Double is null");

        BigDecimal defaultBigDecimal = SimpleTypes.generateDefaultValue(BigDecimal.class);
        Assert.isTrue(defaultBigDecimal != null, "Default BigDecimal is null");

        BigInteger defaultBigInteger = SimpleTypes.generateDefaultValue(BigInteger.class);
        Assert.isTrue(defaultBigInteger != null, "Default BigInteger is null");

        LocalDate defaultLocalDate = SimpleTypes.generateDefaultValue(LocalDate.class);
        Assert.isTrue(defaultLocalDate != null, "Default LocalDate is null");

        LocalDateTime defaultLocalDateTime = SimpleTypes.generateDefaultValue(LocalDateTime.class);
        Assert.isTrue(defaultLocalDateTime != null, "Default LocalDateTime is null");

        LocalTime defaultLocalTime = SimpleTypes.generateDefaultValue(LocalTime.class);
        Assert.isTrue(defaultLocalTime != null, "Default LocalTime is null");

        OffsetDateTime defaultOffsetDateTime = SimpleTypes.generateDefaultValue(OffsetDateTime.class);
        Assert.isTrue(defaultOffsetDateTime != null, "Default OffsetDateTime is null");

        OffsetTime defaultOffsetTime = SimpleTypes.generateDefaultValue(OffsetTime.class);
        Assert.isTrue(defaultOffsetTime != null, "Default OffsetTime is null");

        ZonedDateTime defaultZonedDateTime = SimpleTypes.generateDefaultValue(ZonedDateTime.class);
        Assert.isTrue(defaultZonedDateTime != null, "Default ZonedDateTime is null");


    }


}
