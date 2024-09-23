package argonath.reflector.generator;

import argonath.reflector.generator.model.GeneratorTemplate;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static argonath.reflector.generator.Generators.*;

/**
 * Registry of built-in generator templates. Generator templates are used to build generators from String expressions.
 */

public class GeneratorTemplateRegistry {
    private GeneratorTemplateRegistry() {
    }

    private static Map<String, GeneratorTemplate<?>> generatorRegistry = new HashMap<>();

    // Constants for generator names
    public static final String RANDOM_STRING = "randomString";
    public static final String RANDOM_INT = "randomInt";
    public static final String RANDOM_SHORT = "randomShort";
    public static final String RANDOM_BYTE = "randomByte";
    public static final String RANDOM_CHAR = "randomChar";
    public static final String RANDOM_LONG = "randomLong";
    public static final String RANDOM_DOUBLE = "randomDouble";
    public static final String RANDOM_FLOAT = "randomFloat";
    public static final String RANDOM_BIG_INTEGER = "randomBigInteger";
    public static final String RANDOM_BIG_DECIMAL = "randomBigDecimal";
    public static final String RANDOM_BOOLEAN = "randomBoolean";
    public static final String RANDOM_LOCAL_DATE = "randomLocalDate";
    public static final String RANDOM_LOCAL_DATE_TIME = "randomLocalDateTime";
    public static final String RANDOM_OFFSET_DATE_TIME = "randomOffsetDateTime";
    public static final String RANDOM_OFFSET_TIME = "randomOffsetTime";
    public static final String RANDOM_ZONED_DATE_TIME = "randomZonedDateTime";
    public static final String RANDOM_BYTE_ARRAY = "randomByteArray";
    public static final String NOW = "now";
    public static final String TODAY = "today";
    public static final String LOREM_IPSUM = "loremIpsum";
    public static final String SEQUENCE = "sequence";
    public static final String VALUE_SELECTOR = "valueSelector";
    public static final String ENUM_SELECTOR = "enumSelector";
    public static final String CODE_LIST = "codeList";
    public static final String CODE_LIST_DESCRIPTION = "codeListDescription";

    @SuppressWarnings("unchecked")
    public static <T> GeneratorTemplate<T> generator(String name) {
        return (GeneratorTemplate<T>) generatorRegistry.get(name);
    }

    static {
        registerBuiltinGenerators();
    }

    private static void registerBuiltinGenerators() {
        generatorRegistry.put(RANDOM_STRING, randomStringTemplate());
        generatorRegistry.put(RANDOM_INT, randomIntTemplate());
        generatorRegistry.put(RANDOM_SHORT, randomShortTemplate());
        generatorRegistry.put(RANDOM_BYTE, randomByteTemplate());
        generatorRegistry.put(RANDOM_CHAR, randomCharTemplate());
        generatorRegistry.put(RANDOM_LONG, randomLongTemplate());
        generatorRegistry.put(RANDOM_DOUBLE, randomDoubleTemplate());
        generatorRegistry.put(RANDOM_FLOAT, randomFloatTemplate());
        generatorRegistry.put(RANDOM_BIG_INTEGER, randomBigIntegerTemplate());
        generatorRegistry.put(RANDOM_BIG_DECIMAL, randomBigDecimalTemplate());
        generatorRegistry.put(RANDOM_BOOLEAN, randomBooleanTemplate());
        generatorRegistry.put(RANDOM_LOCAL_DATE, randomLocalDateTemplate());
        generatorRegistry.put(RANDOM_LOCAL_DATE_TIME, randomLocalDateTimeTemplate());
        generatorRegistry.put(RANDOM_OFFSET_DATE_TIME, randomOffsetDateTimeTemplate());
        generatorRegistry.put(RANDOM_OFFSET_TIME, randomOffsetTimeTemplate());
        generatorRegistry.put(RANDOM_ZONED_DATE_TIME, randomZonedDateTimeTemplate());
        generatorRegistry.put(RANDOM_BYTE_ARRAY, randomByteArrayTemplate());
        generatorRegistry.put(NOW, nowTemplate());
        generatorRegistry.put(TODAY, todayTemplate());
        generatorRegistry.put(LOREM_IPSUM, loremIpsumTemplate());
        generatorRegistry.put(SEQUENCE, sequenceTemplate());
        generatorRegistry.put(VALUE_SELECTOR, valueSelectorTemplate());
        generatorRegistry.put(ENUM_SELECTOR, enumSelectorTemplate());
        generatorRegistry.put(CODE_LIST, codeListTemplate());
        generatorRegistry.put(CODE_LIST_DESCRIPTION, codeListDescriptionTemplate());
    }

    private static GeneratorTemplate<String> randomStringTemplate() {
        return args -> {
            String randomString = parseString(args, 0, RANDOM_STRING);
            return randomString(randomString);
        };
    }

    private static GeneratorTemplate<Integer> randomIntTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_INT);
            return randomInt(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Short> randomShortTemplate() {
        return args -> {
            Pair<Short, Short> range = parseShortRange(args, RANDOM_SHORT);
            return randomShort(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Byte> randomByteTemplate() {
        return args -> {
            Pair<Byte, Byte> range = parseByteRange(args, RANDOM_BYTE);
            return randomByte(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Character> randomCharTemplate() {
        return args -> {
            Pair<Character, Character> range = parseCharRange(args, RANDOM_CHAR);
            return randomCharacter(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Long> randomLongTemplate() {
        return args -> {
            Pair<Long, Long> range = parseLongRange(args, RANDOM_LONG);
            return randomLong(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Double> randomDoubleTemplate() {
        return args -> {
            Pair<Double, Double> range = parseDoubleRange(args, RANDOM_DOUBLE);
            return randomDouble(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Float> randomFloatTemplate() {
        return args -> {
            Pair<Float, Float> range = parseFloatRange(args, RANDOM_FLOAT);
            return randomFloat(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<BigInteger> randomBigIntegerTemplate() {
        return args -> {
            Pair<Long, Long> range = parseLongRange(args, RANDOM_BIG_INTEGER);
            return randomBigInteger(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<BigDecimal> randomBigDecimalTemplate() {
        return args -> {
            Pair<Double, Double> range = parseDoubleRange(args, RANDOM_BIG_DECIMAL);
            return randomBigDecimal(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<Boolean> randomBooleanTemplate() {
        return args -> randomBoolean();
    }

    private static GeneratorTemplate<LocalDate> randomLocalDateTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_LOCAL_DATE);
            return randomLocalDate(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<LocalDateTime> randomLocalDateTimeTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_LOCAL_DATE_TIME);
            return randomLocalDateTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<OffsetDateTime> randomOffsetDateTimeTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_OFFSET_DATE_TIME);
            return randomOffsetDateTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<OffsetTime> randomOffsetTimeTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_OFFSET_TIME);
            return randomOffsetTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<ZonedDateTime> randomZonedDateTimeTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_ZONED_DATE_TIME);
            return randomZonedDateTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<byte[]> randomByteArrayTemplate() {
        return args -> {
            Pair<Integer, Integer> range = parseIntRange(args, RANDOM_BYTE_ARRAY);
            return randomByteArray(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate<LocalDateTime> nowTemplate() {
        return args -> now();
    }

    private static GeneratorTemplate<LocalDate> todayTemplate() {
        return args -> today();
    }

    private static GeneratorTemplate<String> loremIpsumTemplate() {
        return args -> {
            int minSize = parseInt(args, 0, LOREM_IPSUM);
            if (args.length == 1) {
                return loremIpsum(minSize);
            }
            int maxSize = parseInt(args, 1, LOREM_IPSUM);
            return loremIpsum(minSize, maxSize);
        };
    }

    private static GeneratorTemplate<Integer> sequenceTemplate() {
        return args -> {
            int start = parseInt(args, 0, SEQUENCE);
            int step = parseInt(args, 1, SEQUENCE);
            return sequence(start, step);
        };
    }

    private static GeneratorTemplate<String> valueSelectorTemplate() {
        return args -> {
            boolean allowDuplicates = parseBoolean(args, 0, VALUE_SELECTOR);
            String[] values = parseVarargs(args, 1);
            return valueSelector(allowDuplicates, values);
        };
    }

    private static GeneratorTemplate<? extends Enum> enumSelectorTemplate() {
        return args -> {
            Class<? extends Enum> enumClass = parseEnum(args, 0, ENUM_SELECTOR);
            boolean allowDuplicates = parseBoolean(args, 1, ENUM_SELECTOR);
            return enumValueSelector(enumClass, allowDuplicates);
        };
    }


    private static String parseString(String[] args, int index, String generatorName) {
        if (args.length <= index) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires at least " + (index + 1) + " arguments");
        }
        return args[index].trim();
    }

    private static Pair<Integer, Integer> parseIntRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of(Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        int firstArg = parseInt(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of(0, firstArg);
        }
        int secondArg = parseInt(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static int parseInt(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        //try-catch block to handle NumberFormatException
        try {
            return Integer.parseInt(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires an integer argument at position " + (index + 1));
        }
    }

    private static Pair<Short, Short> parseShortRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of(Short.MIN_VALUE, Short.MAX_VALUE);
        }
        short firstArg = parseShort(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of((short) 0, firstArg);
        }
        short secondArg = parseShort(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static short parseShort(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Short.parseShort(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a short argument at position " + (index + 1));
        }
    }

    // parseByteRange
    private static Pair<Byte, Byte> parseByteRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of(Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
        byte firstArg = parseByte(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of((byte) 0, firstArg);
        }
        byte secondArg = parseByte(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static byte parseByte(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);
        try {
            return Byte.parseByte(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a byte argument at position " + (index + 1));
        }
    }

    private static Pair<Character, Character> parseCharRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of('A', 'z');
        }
        char firstArg = parseChar(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of('a', firstArg);
        }
        char secondArg = parseChar(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static char parseChar(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);
        if (strVal.length() != 1) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a char argument at position " + (index + 1));
        }
        return strVal.charAt(0);
    }

    private static Pair<Long, Long> parseLongRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of(Long.MIN_VALUE, Long.MAX_VALUE);
        }
        long firstArg = parseLong(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of(0L, firstArg);
        }
        long secondArg = parseLong(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static long parseLong(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Long.parseLong(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a long argument at position " + (index + 1));
        }
    }

    private static Pair<Double, Double> parseDoubleRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of(Double.MIN_VALUE, Double.MAX_VALUE);
        }
        double firstArg = parseDouble(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of(0.0, firstArg);
        }
        double secondArg = parseDouble(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static double parseDouble(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Double.parseDouble(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a double argument at position " + (index + 1));
        }
    }

    private static Pair<Float, Float> parseFloatRange(String[] args, String generatorName) {
        if (args.length == 0) {
            return Pair.of(Float.MIN_VALUE, Float.MAX_VALUE);
        }
        float firstArg = parseFloat(args, 0, generatorName);
        if (args.length == 1) {
            return Pair.of(0.0f, firstArg);
        }
        float secondArg = parseFloat(args, 1, generatorName);
        if (args.length == 2) {
            return Pair.of(firstArg, secondArg);
        }
        throw new IllegalArgumentException("Generator " + generatorName + " requires at most 2 arguments");
    }

    private static float parseFloat(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Float.parseFloat(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a float argument at position " + (index + 1));
        }
    }

    private static boolean parseBoolean(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);
        if (!strVal.equals("true") && !strVal.equals("false")) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a boolean argument at position " + (index + 1));
        }
        return Boolean.parseBoolean(strVal);
    }

    private static String[] parseVarargs(String[] args, int startIndex) {
        if (args.length <= 1) {
            throw new IllegalArgumentException("Generator valueSelector requires at least 1 value argument");
        }
        if (startIndex >= args.length) {
            throw new IllegalArgumentException("Invalid start index for varargs: " + startIndex + " (args length: " + args.length + ")");
        }
        String[] varargs = new String[args.length - startIndex];
        System.arraycopy(args, startIndex, varargs, 0, varargs.length);
        return varargs;
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Enum> parseEnum(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);
        try {
            return (Class<? extends Enum>) Class.forName(strVal);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid enum class: " + strVal);
        }
    }

    private static GeneratorTemplate<String> codeListTemplate() {
        return args -> {
            String codeListName = parseString(args, 0, CODE_LIST);
            boolean withReplacement = parseBoolean(args, 1, CODE_LIST);
            return codeListValue(String.class, codeListName, withReplacement);
        };
    }

    private static GeneratorTemplate<String> codeListDescriptionTemplate() {
        return args -> {
            String codeListName = parseString(args, 0, CODE_LIST_DESCRIPTION);
            return codeListDescription(codeListName);
        };
    }

}
