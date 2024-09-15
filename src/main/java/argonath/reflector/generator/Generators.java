package argonath.reflector.generator;

import argonath.random.*;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.utils.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Set;
import java.util.function.Function;

public class Generators {
    public static Generator<String> randomString(String format) {
        return create(String.class, (seed) -> StringGenerator.randomString(format));
    }

    public static Generator<Integer> randomInt(int min, int max) {
        return create(Integer.class, (seed) -> RandomNumber.getInteger(min, max));
    }

    public static Generator<Short> randomShort(short min, short max) {
        return create(Short.class, (seed) -> RandomNumber.getShort(min, max));
    }

    public static Generator<Byte> randomByte(byte min, byte max) {
        return create(Byte.class, (seed) -> RandomNumber.getByte(min, max));
    }

    public static Generator<Character> randomCharacter(char min, char max) {
        return create(Character.class, (seed) -> RandomNumber.getCharacter(min, max));
    }

    public static Generator<Long> randomLong(long min, long max) {
        return create(Long.class, (seed) -> RandomNumber.getLong(min, max));
    }

    public static Generator<Double> randomDouble(double min, double max) {
        return create(Double.class, (seed) -> RandomNumber.getDouble(min, max));
    }

    public static Generator<Float> randomFloat(float min, float max) {
        return create(Float.class, (seed) -> RandomNumber.getFloat(min, max));
    }

    public static Generator<BigInteger> randomBigInteger(long min, long max) {
        return create(BigInteger.class, (seed) -> BigInteger.valueOf(RandomNumber.getLong(min, max)));
    }

    public static Generator<BigDecimal> randomBigDecimal(double min, double max) {
        return create(BigDecimal.class, (seed) -> BigDecimal.valueOf(RandomNumber.getDouble(min, max)));
    }

    public static Generator<LocalDate> randomLocalDate(int min, int max) {
        return create(LocalDate.class, (seed) -> RandomDates.randomLocalDate(min, max));
    }

    public static Generator<LocalDateTime> randomLocalDateTime(int minusDays, int plusDays) {
        return create(LocalDateTime.class, (seed) -> RandomDates.randomLocalDateTime(minusDays, plusDays));
    }

    public static Generator<OffsetDateTime> randomOffsetDateTime(int minusDays, int plusDays) {
        return create(OffsetDateTime.class, (seed) -> RandomDates.randomOffsetDateTime(minusDays, plusDays));
    }

    public static Generator<OffsetTime> randomOffsetTime(int minusSeconds, int plusSeconds) {
        return create(OffsetTime.class, (seed) -> RandomDates.randomOffsetTime(minusSeconds, plusSeconds));
    }

    public static Generator<ZonedDateTime> randomZonedDateTime(int minusDays, int plusDays) {
        return create(ZonedDateTime.class, (seed) -> RandomDates.randomZonedDateTime(minusDays, plusDays));
    }

    public static Generator<byte[]> randomByteArray(int minSize, int maxSize) {
        return create(byte[].class, (seed) -> RandomIterable.randomByteArray(minSize, maxSize));
    }


    public static Generator<Boolean> randomBoolean() {
        return create(Boolean.class, (seed) -> RandomNumber.flipCoin());
    }

    public static Generator<LocalDateTime> now() {
        return create(LocalDateTime.class, (seed) -> LocalDateTime.now());
    }

    public static Generator<LocalDate> today() {
        return create(LocalDate.class, (seed) -> LocalDate.now());
    }

    public static Generator<String> loremIpsum(int length) {
        return create(String.class, (seed) -> TextGenerator.loremIpsum(length));
    }

    public static Generator<String> loremIpsum(int minLength, int maxLength) {
        return create(String.class, (seed) -> TextGenerator.loremIpsum(minLength, maxLength));
    }

    /**
     * Generate a sequence of integers starting from the given start value and incrementing by the given step.
     * The sequence is common for all generated elements at the same path of the object graph (regardless the field selection method)
     */
    public static Generator<Integer> sequence(int start, int step) {
        return new SequenceGenerator(start, step);
    }

    public static Generator<?> fixedValue(String value) {
        return create(Object.class, (seed) -> value);
    }


    public static <T> Generator<T> valueSelector(boolean withReplacement, Set<T> values) {
        Assert.notNull(values, "Values set cannot be null");
        Class<T> clazz = ReflectiveAccessor.getClassFromCollectionTyped(values);
        return new ValueGenerator<>(clazz, withReplacement, values);
    }

    public static <T> Generator<T> valueSelector(boolean withReplacement, T... values) {
        return valueSelector(withReplacement, Set.of(values));
    }

    // enum values
    public static <T extends Enum<T>> Generator<T> enumValueSelector(Class<T> enumClass, boolean withReplacement) {
        return new EnumValueGenerator<>(enumClass, withReplacement);
    }

    private static <T> Generator<T> create(Class<T> clazz, Function<Long, T> generator) {
        return new Generator<T>() {
            @Override
            public T generate(Long seed) {
                return generator.apply(seed);
            }

            @Override
            public Class<T> type() {
                return clazz;
            }
        };
    }

}