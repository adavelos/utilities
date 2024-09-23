package argonath.reflector.generator;

import argonath.random.*;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.generator.types.*;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.utils.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Set;
import java.util.function.Function;

public class Generators {
    private Generators() {
    }

    /**
     * Generates a random string based on the provided format.
     * The format convention is the following:
     * - 'a' for any letter (lowercase or uppercase)
     * - 'n' for any digit
     * - 'an' for any letter or digit (alphanumeric)
     * <p>
     * Examples for specifying the size of the String:
     * - 'an200' for exactly 200 letters
     * - 'an..200' for up to 200 letters
     * - 'an100..200' for between 100 and 200 letters
     *
     * @param format The format of the string to be generated.
     * @return A generator that produces random strings.
     */
    public static Generator<String> randomString(String format) {
        return create(String.class, seed -> StringGenerator.randomString(format));
    }

    /**
     * Generates a random integer within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range (excluded)
     * @return A generator that produces random integers.
     */
    public static Generator<Integer> randomInt(int min, int max) {
        return create(Integer.class, seed -> RandomNumber.getInteger(min, max));
    }

    /**
     * Generates a random short within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random shorts.
     */
    public static Generator<Short> randomShort(short min, short max) {
        return create(Short.class, seed -> RandomNumber.getShort(min, max));
    }

    /**
     * Generates a random byte within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random bytes.
     */
    public static Generator<Byte> randomByte(byte min, byte max) {
        return create(Byte.class, seed -> RandomNumber.getByte(min, max));
    }

    /**
     * Generates a random character within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random characters.
     */
    public static Generator<Character> randomCharacter(char min, char max) {
        return create(Character.class, seed -> RandomNumber.getCharacter(min, max));
    }

    /**
     * Generates a random long within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random longs.
     */
    public static Generator<Long> randomLong(long min, long max) {
        return create(Long.class, seed -> RandomNumber.getLong(min, max));
    }

    /**
     * Generates a random double within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random doubles.
     */
    public static Generator<Double> randomDouble(double min, double max) {
        return create(Double.class, seed -> RandomNumber.getDouble(min, max));
    }

    /**
     * Generates a random float within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random floats.
     */
    public static Generator<Float> randomFloat(float min, float max) {
        return create(Float.class, seed -> RandomNumber.getFloat(min, max));
    }

    /**
     * Generates a random BigInteger within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random BigIntegers.
     */
    public static Generator<BigInteger> randomBigInteger(long min, long max) {
        return create(BigInteger.class, seed -> BigInteger.valueOf(RandomNumber.getLong(min, max)));
    }

    /**
     * Generates a random BigDecimal within the provided range.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A generator that produces random BigDecimals.
     */
    public static Generator<BigDecimal> randomBigDecimal(double min, double max) {
        return create(BigDecimal.class, seed -> BigDecimal.valueOf(RandomNumber.getDouble(min, max)));
    }

    /**
     * Generates a random LocalDate within the provided range of years.
     * The 'max' value is excluded from the range.
     *
     * @param min The minimum year.
     * @param max The maximum year (exclusive)
     * @return A generator that produces random LocalDates.
     */
    public static Generator<LocalDate> randomLocalDate(int min, int max) {
        return create(LocalDate.class, seed -> RandomDates.randomLocalDate(min, max));
    }

    /**
     * Generates a random LocalDateTime within the provided range of days.
     * The 'plusDays' value is excluded from the range.
     *
     * @param minusDays The number of days to subtract from the current date.
     * @param plusDays  The number of days to add to the current date. (exclusive)
     * @return A generator that produces random LocalDateTimes.
     */
    public static Generator<LocalDateTime> randomLocalDateTime(int minusDays, int plusDays) {
        return create(LocalDateTime.class, seed -> RandomDates.randomLocalDateTime(minusDays, plusDays));
    }

    /**
     * Generates a random OffsetDateTime within the provided range of days.
     * The 'plusDays' value is excluded from the range.
     *
     * @param minusDays The number of days to subtract from the current date.
     * @param plusDays  The number of days to add to the current date. (exclusive)
     * @return A generator that produces random OffsetDateTimes.
     */
    public static Generator<OffsetDateTime> randomOffsetDateTime(int minusDays, int plusDays) {
        return create(OffsetDateTime.class, seed -> RandomDates.randomOffsetDateTime(minusDays, plusDays));
    }

    /**
     * Generates a random OffsetTime within the provided range of seconds.
     * The 'plusSeconds' value is excluded from the range.
     *
     * @param minusSeconds The number of seconds to subtract from the current time.
     * @param plusSeconds  The number of seconds to add to the current time. (exclusive)
     * @return A generator that produces random OffsetTimes.
     */
    public static Generator<OffsetTime> randomOffsetTime(int minusSeconds, int plusSeconds) {
        return create(OffsetTime.class, seed -> RandomDates.randomOffsetTime(minusSeconds, plusSeconds));
    }

    /**
     * Generates a random ZonedDateTime within the provided range of days.
     * The 'plusDays' value is excluded from the range.
     *
     * @param minusDays The number of days to subtract from the current date.
     * @param plusDays  The number of days to add to the current date. (exclusive)
     * @return A generator that produces random ZonedDateTimes.
     */
    public static Generator<ZonedDateTime> randomZonedDateTime(int minusDays, int plusDays) {
        return create(ZonedDateTime.class, seed -> RandomDates.randomZonedDateTime(minusDays, plusDays));
    }

    /**
     * Generates a random byte array within the provided size range.
     * The 'maxSize' value is excluded from the range.
     *
     * @param minSize The minimum size of the byte array.
     * @param maxSize The maximum size of the byte array. (exclusive)
     * @return A generator that produces random byte arrays.
     */
    public static Generator<byte[]> randomByteArray(int minSize, int maxSize) {
        return create(byte[].class, seed -> RandomIterable.randomByteArray(minSize, maxSize));
    }

    /**
     * Generates a random boolean value.
     *
     * @return A generator that produces random booleans.
     */
    public static Generator<Boolean> randomBoolean() {
        return create(Boolean.class, seed -> RandomNumber.flipCoin());
    }

    /**
     * Generates the current date/time (<code>LocalDateTime.now()</code>).
     *
     * @return A generator that produces the current LocalDateTime.
     */
    public static Generator<LocalDateTime> now() {
        return create(LocalDateTime.class, seed -> LocalDateTime.now());
    }

    /**
     * Generates the current date (<code>LocalTime.now()</code>).
     *
     * @return A generator that produces the current LocalDate.
     */
    public static Generator<LocalDate> today() {
        return create(LocalDate.class, seed -> LocalDate.now());
    }

    /**
     * Generates a Lorem Ipsum text of the specified length.
     *
     * @param length The length of the Lorem Ipsum text.
     * @return A generator that produces Lorem Ipsum text.
     */
    public static Generator<String> loremIpsum(int length) {
        return create(String.class, seed -> TextGenerator.loremIpsum(length));
    }

    /**
     * Generates a Lorem Ipsum text with a length between the specified minimum and maximum.
     * The 'maxLength' value is excluded from the range.
     *
     * @param minLength The minimum length of the Lorem Ipsum text.
     * @param maxLength The maximum length of the Lorem Ipsum text. (exclusive)
     * @return A generator that produces Lorem Ipsum text.
     */
    public static Generator<String> loremIpsum(int minLength, int maxLength) {
        return create(String.class, seed -> TextGenerator.loremIpsum(minLength, maxLength));
    }

    /**
     * Generates a sequence of integers starting from the given start value and incrementing by the given step.
     * The sequence is shared for all generated elements at the same path of the object graph.
     * (regardless the Field Selector type that is used to decide the generator)
     * <p>
     * Note: The generator is stateful. It keeps track of the last generated value for each path.
     *
     * @param start The starting value of the sequence.
     * @param step  The increment step of the sequence.
     * @return A generator that produces a sequence of integers.
     */
    public static Generator<Integer> sequence(int start, int step) {
        return new SequenceGenerator(start, step);
    }

    /**
     * Generates a fixed string value.
     * It is not required to assign the fixed value on a String field. It can be assigned to any field type, provided
     * that the type is registered as a <code>SimpleType</code>, hence it defines a String-to-Type conversion method.
     * (all common types, i.e. int, short, String etc. are registered by default)
     *
     * @param value The fixed string value to be generated.
     * @return A generator that produces the fixed string value.
     */
    public static Generator<String> fixedValue(String value) {
        return create(String.class, seed -> value);
    }

    /**
     * Generates values from a given set.
     * <p>
     * When 'withReplacement' is set to true, the same value can be selected multiple times.
     * When 'withReplacement' is set to false, each value is selected only once.
     * <p>
     * Note:
     * - The latter version is stateful and keeps track of selected values.
     * - When values are exhausted then the generator resets its state and starts again from the beginning.
     *
     * @param <T>             The type of values in the set.
     * @param withReplacement Whether to allow repeated selection of the same value.
     * @param values          The set of values to select from.
     * @return A generator that produces values from the given set.
     */
    public static <T> Generator<T> valueSelector(boolean withReplacement, Set<T> values) {
        Assert.notNull(values, "Values set cannot be null");
        Class<T> clazz = ReflectiveAccessor.getClassFromCollectionTyped(values);
        return new ValueGenerator<>(clazz, withReplacement, values);
    }

    /**
     * Generates values from a given array, with or without replacement.
     * <p>
     * When 'withReplacement' is set to true, the same value can be selected multiple times.
     * When 'withReplacement' is set to false, each value is selected only once.
     * <p>
     * Note:
     * - The latter version is stateful and keeps track of selected values.
     * - When values are exhausted then the generator resets its state and starts again from the beginning.
     *
     * @param <T>             The type of values in the array.
     * @param withReplacement Whether to allow repeated selection of the same value.
     * @param values          The array of values to select from.
     * @return A generator that produces values from the given array.
     */
    public static <T> Generator<T> valueSelector(boolean withReplacement, T... values) {
        return valueSelector(withReplacement, Set.of(values));
    }

    /**
     * Generates enum values from a given enum class, with or without replacement.
     * <p>
     * When 'withReplacement' is set to true, the same value can be selected multiple times.
     * When 'withReplacement' is set to false, each value is selected only once.
     * <p>
     * Note:
     * - The latter version is stateful and keeps track of selected values.
     * - When values are exhausted then the generator resets its state and starts again from the beginning.
     *
     * @param <T>             The enum type.
     * @param enumClass       The enum class to select values from.
     * @param withReplacement Whether to allow repeated selection of the same value.
     * @return A generator that produces enum values.
     */
    public static <T extends Enum<T>> Generator<T> enumValueSelector(Class<T> enumClass, boolean withReplacement) {
        return new EnumValueGenerator<>(enumClass, withReplacement);
    }

    /**
     * Generates code list values of a specified type, with or without replacement.
     * Code List data are loaded from dedicated files (=code list specs files).
     * <p/>
     * There are two types of source data: Global Code Lists registered once and available for the entire process, generated
     * by the <code>GlobalCodeLists.load()</code> method and Code List files injected into the Specific instance of the
     * Object Generator, by the <code>ObjectGenerator.withCodeListSpecs()</code> method.
     * <p/>
     * See: <code>CodeLists</code> class documentation for details on the format of the code list files.
     *
     * @param <T>             The type of the code list values.
     * @param clazz           The class of the code list values.
     * @param key             The key for the code list.
     * @param withReplacement Whether to allow repeated selection of the same value.
     * @return A generator that produces values selected from a code list.
     */
    public static <T> Generator<T> codeListValue(Class<T> clazz, String key, boolean withReplacement) {
        return new CodeListValueGenerator<>(clazz, key, withReplacement);
    }

    /**
     * Generates code list descriptions for a given key. Since descriptions are paired with codes, there is a convention
     * for automatically selecting descriptions conveniently: a code list description is always used in the same level
     * with the respective code field. With this assumption, the description generator must be used in combination with
     * a code list value generator. The generator keeps track of the last selected code list item and returns the
     * description.
     *
     * @param key The key for the code list.
     * @return A generator that produces descriptions of values selected from a code list.
     */
    public static Generator<String> codeListDescription(String key) {
        return new CodeListDescriptionGenerator(key);
    }

    /**
     * Overloaded method of the <code>codeListValue()</code> generator, generates string values from a Code List.
     *
     * @param key             The key for the code list.
     * @param withReplacement Whether to allow repeated selection of the same value.
     * @return A generator that produces integer values selected from a code list.
     */
    public static Generator<Integer> intCodeListValue(String key, boolean withReplacement) {
        return new CodeListValueGenerator<>(Integer.class, key, withReplacement);
    }

    /**
     * Overloaded method of the <code>codeListValue()</code> generator, generates string values from a Code List.
     *
     * @param key             The key for the code list.
     * @param withReplacement Whether to allow repeated selection of the same value.
     * @return A generator that produces string values selected from a code list.
     */
    public static Generator<String> stringCodeListValue(String key, boolean withReplacement) {
        return new CodeListValueGenerator<>(String.class, key, withReplacement);
    }

    private static <T> Generator<T> create(Class<T> clazz, Function<Long, T> generator) {
        return new Generator<T>() {
            @Override
            public T generate(GeneratorContext ctx) {
                Long seed = ctx != null ? ctx.seed() : null;
                return generator.apply(seed);
            }

            @Override
            public Class<T> type() {
                return clazz;
            }
        };
    }
}