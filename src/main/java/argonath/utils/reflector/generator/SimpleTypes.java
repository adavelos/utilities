package argonath.utils.reflector.generator;

import argonath.utils.Assert;
import argonath.utils.random.RandomNumber;
import argonath.utils.random.ValuesGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleTypes {

    private static final Set<Class> PRIMITIVE_TYPES = new HashSet<>(Arrays.asList(
            String.class,
            Boolean.class,
            Character.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            BigDecimal.class,
            BigInteger.class,
            LocalDate.class,
            LocalDateTime.class,
            LocalTime.class,
            OffsetDateTime.class,
            OffsetTime.class,
            ZonedDateTime.class
    ));

    public static Boolean isSimpleType(Class<?> clazz) {
        Assert.notNull(clazz, "Cannot determine SimpleType from 'null' Class");
        return clazz.isPrimitive() || clazz.isEnum() || PRIMITIVE_TYPES.contains(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T generateDefaultValue(Class<T> clazz) {
        // for each supported primitive type, return instantiate
        if (clazz.isAssignableFrom(String.class)) {
            return (T) defaultString();
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return (T) defaultBoolean();
        } else if (clazz.isAssignableFrom(Character.class)) {
            return (T) defaultCharacter();
        } else if (clazz.isAssignableFrom(Byte.class)) {
            return (T) defaultByte();
        } else if (clazz.isAssignableFrom(Short.class)) {
            return (T) defaultShort();
        } else if (clazz.isAssignableFrom(Integer.class)) {
            return (T) defaultInteger();
        } else if (clazz.isAssignableFrom(Long.class)) {
            return (T) defaultLong();
        } else if (clazz.isAssignableFrom(Float.class)) {
            return (T) defaultFloat();
        } else if (clazz.isAssignableFrom(Double.class)) {
            return (T) defaultDouble();
        } else if (clazz.isAssignableFrom(BigDecimal.class)) {
            return (T) defaultBigDecimal();
        } else if (clazz.isAssignableFrom(BigInteger.class)) {
            return (T) defaultBigInteger();
        } else if (clazz.isAssignableFrom(LocalDate.class)) {
            return (T) defaultLocalDate();
        } else if (clazz.isAssignableFrom(LocalDateTime.class)) {
            return (T) defaultLocalDateTime();
        } else if (clazz.isAssignableFrom(LocalTime.class)) {
            return (T) defaultLocalTime();
        } else if (clazz.isAssignableFrom(OffsetDateTime.class)) {
            return (T) defaultOffsetDateTime();
        } else if (clazz.isAssignableFrom(OffsetTime.class)) {
            return (T) defaultOffsetTime();
        } else if (clazz.isAssignableFrom(ZonedDateTime.class)) {
            return (T) defaultZonedDateTime();
        } else {
            throw new RuntimeException("Unsupported SimpleType: " + clazz.getName());
        }
    }

    private static String defaultString() {
        return ValuesGenerator.randomString("an10");
    }

    // generate all defaultX() methods
    private static Boolean defaultBoolean() {
        return RandomNumber.throwDice();
    }

    private static Character defaultCharacter() {
        String charStr = ValuesGenerator.randomString("an1");
        return Character.valueOf(charStr.charAt(0));
    }

    private static Byte defaultByte() {
        String str = ValuesGenerator.randomString("n1");
        return Byte.valueOf(str);
    }

    private static Short defaultShort() {
        return RandomNumber.getInteger(Short.MIN_VALUE, Short.MAX_VALUE).shortValue();
    }

    private static Integer defaultInteger() {
        return RandomNumber.getInteger(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static Long defaultLong() {
        return RandomNumber.getLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static Float defaultFloat() {
        String str1 = ValuesGenerator.randomString("n3");
        String str2 = ValuesGenerator.randomString("n1");
        String str = str1 + "." + str2;
        return Float.parseFloat(str);
    }

    private static Double defaultDouble() {
        String str1 = ValuesGenerator.randomString("n3");
        String str2 = ValuesGenerator.randomString("n1");
        String str = str1 + "." + str2;
        return Double.parseDouble(str);
    }

    private static BigDecimal defaultBigDecimal() {
        return new BigDecimal(defaultDouble());
    }

    private static BigInteger defaultBigInteger() {
        return BigInteger.valueOf(defaultLong());
    }

    private static LocalDate defaultLocalDate() {
        int yearShift = RandomNumber.getInteger(-10, 10);
        int monthShift = RandomNumber.getInteger(-10, 10);
        int dayShift = RandomNumber.getInteger(-165, 165);
        return LocalDate.now().plusYears(yearShift).plusMonths(monthShift).plusDays(dayShift);
    }

    private static LocalDateTime defaultLocalDateTime() {
        int yearShift = RandomNumber.getInteger(-10, 10);
        int monthShift = RandomNumber.getInteger(-10, 10);
        int dayShift = RandomNumber.getInteger(-165, 165);
        int hourShift = RandomNumber.getInteger(-23, 23);
        int minuteShift = RandomNumber.getInteger(-59, 59);
        int secondShift = RandomNumber.getInteger(-59, 59);
        return LocalDateTime.now().plusYears(yearShift).plusMonths(monthShift).plusDays(dayShift)
                .plusHours(hourShift).plusMinutes(minuteShift).plusSeconds(secondShift);
    }

    private static LocalTime defaultLocalTime() {
        int hourShift = RandomNumber.getInteger(-23, 23);
        int minuteShift = RandomNumber.getInteger(-59, 59);
        int secondShift = RandomNumber.getInteger(-59, 59);
        return LocalTime.now().plusHours(hourShift).plusMinutes(minuteShift).plusSeconds(secondShift);
    }

    private static OffsetDateTime defaultOffsetDateTime() {
        LocalDateTime localDateTime = defaultLocalDateTime();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(RandomNumber.getInteger(-12, 12));
        return OffsetDateTime.of(localDateTime, zoneOffset);
    }

    private static OffsetTime defaultOffsetTime() {
        LocalTime localTime = defaultLocalTime();
        ZoneOffset zoneOffset = ZoneOffset.ofHours(RandomNumber.getInteger(-2, 2));
        return OffsetTime.of(localTime, zoneOffset);
    }

    private static ZonedDateTime defaultZonedDateTime() {
        LocalDateTime localDateTime = defaultLocalDateTime();
        ZoneId zoneId = ZoneId.systemDefault();
        return ZonedDateTime.of(localDateTime, zoneId);
    }

}
