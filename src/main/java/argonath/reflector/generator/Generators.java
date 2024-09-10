package argonath.reflector.generator;

import argonath.random.*;
import argonath.reflector.generator.model.Generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.function.BiFunction;

public class Generators {
    public static Generator<String> randomString(String format) {
        return create(String.class, (seed, args) -> StringGenerator.randomString(format));
    }

    public static Generator<Integer> randomInt(int min, int max) {
        return create(Integer.class, (seed, args) -> RandomNumber.getInteger(min, max));
    }

    public static Generator<Short> randomShort(short min, short max) {
        return create(Short.class, (seed, args) -> RandomNumber.getShort(min, max));
    }

    public static Generator<Byte> randomByte(byte min, byte max) {
        return create(Byte.class, (seed, args) -> RandomNumber.getByte(min, max));
    }

    public static Generator<Character> randomCharacter(char min, char max) {
        return create(Character.class, (seed, args) -> RandomNumber.getCharacter(min, max));
    }

    public static Generator<Long> randomLong(long min, long max) {
        return create(Long.class, (seed, args) -> RandomNumber.getLong(min, max));
    }

    public static Generator<Double> randomDouble(double min, double max) {
        return create(Double.class, (seed, args) -> RandomNumber.getDouble(min, max));
    }

    public static Generator<Float> randomFloat(float min, float max) {
        return create(Float.class, (seed, args) -> RandomNumber.getFloat(min, max));
    }

    public static Generator<BigInteger> randomBigInteger(long min, long max) {
        return create(BigInteger.class, (seed, args) -> BigInteger.valueOf(RandomNumber.getLong(min, max)));
    }

    public static Generator<BigDecimal> randomBigDecimal(long min, long max) {
        return create(BigDecimal.class, (seed, args) -> BigDecimal.valueOf(RandomNumber.getLong(min, max)));
    }

    public static Generator<LocalDate> randomLocalDate(int min, int max) {
        return create(LocalDate.class, (seed, args) -> RandomDates.randomLocalDate(min, max));
    }

    public static Generator<LocalDateTime> randomLocalDateTime(int minusDays, int plusDays) {
        return create(LocalDateTime.class, (seed, args) -> RandomDates.randomLocalDateTime(minusDays, plusDays));
    }

    public static Generator<OffsetDateTime> randomOffsetDateTime(int minusDays, int plusDays) {
        return create(OffsetDateTime.class, (seed, args) -> RandomDates.randomOffsetDateTime(minusDays, plusDays));
    }

    public static Generator<OffsetTime> randomOffsetTime(int minusHours, int plusHours) {
        return create(OffsetTime.class, (seed, args) -> RandomDates.randomOffsetTime(minusHours, plusHours));
    }

    public static Generator<ZonedDateTime> randomZonedDateTime(int minusDays, int plusDays) {
        return create(ZonedDateTime.class, (seed, args) -> RandomDates.randomZonedDateTime(minusDays, plusDays));
    }

    public static Generator<byte[]> randomByteArray(int minSize, int maxSize) {
        return create(byte[].class, (seed, args) -> RandomIterable.randomByteArray(minSize, maxSize));
    }

    public static Generator<LocalDateTime> now() {
        return create(LocalDateTime.class, (seed, args) -> LocalDateTime.now());
    }

    public static Generator<String> loremIpsum(int length) {
        return create(String.class, (seed, args) -> TextGenerator.loremIpsum(length));
    }

    public static Generator<String> loremIpsum(int minLength, int maxLength) {
        return create(String.class, (seed, args) -> TextGenerator.loremIpsum(minLength, maxLength));
    }

    public static Generator<Integer> sequence(int start, int step) {
        return new SequenceGenerator(start, step, null);
    }

    public static Generator<Integer> sequence(int start, int step, String key) {
        return new SequenceGenerator(start, step, key);
    }

    public static Generator<Boolean> randomBoolean() {
        return create(Boolean.class, (seed, args) -> RandomNumber.flipCoin());
    }

    private static <T> Generator<T> create(Class<T> clazz, BiFunction<Long, Object[], T> generator) {
        return new Generator<T>() {
            @Override
            public T generate(Long seed, Object... args) {
                return generator.apply(seed, args);
            }

            @Override
            public Class<T> type() {
                return clazz;
            }
        };
    }
}