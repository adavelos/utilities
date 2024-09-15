package argonath.reflector.generator;

import argonath.reflector.generator.model.GeneratorTemplate;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

import static argonath.reflector.generator.Generators.*;

/**
 * Registry of built-in generator templates. Generator templates are used to build generators from String expressions.
 */

public class GeneratorTemplateRegistry {

    private static Map<String, GeneratorTemplate> generatorRegistry = new HashMap<>();

    static {
        registerBuiltinGenerators();
    }

    public static GeneratorTemplate generator(String name) {
        return generatorRegistry.get(name);
    }

    private static void registerBuiltinGenerators() {
        generatorRegistry.put("randomString", randomStringTemplate());
        generatorRegistry.put("randomInt", randomIntTemplate());
        generatorRegistry.put("randomShort", randomShortTemplate());
        generatorRegistry.put("randomByte", randomByteTemplate());
        generatorRegistry.put("randomChar", randomCharTemplate());
        generatorRegistry.put("randomLong", randomLongTemplate());
        generatorRegistry.put("randomDouble", randomDoubleTemplate());
        generatorRegistry.put("randomFloat", randomFloatTemplate());
        generatorRegistry.put("randomBigInteger", randomBigIntegerTemplate());
        generatorRegistry.put("randomBigDecimal", randomBigDecimalTemplate());
        generatorRegistry.put("randomBoolean", randomBooleanTemplate());
        generatorRegistry.put("randomLocalDate", randomLocalDateTemplate());
        generatorRegistry.put("randomLocalDateTime", randomLocalDateTimeTemplate());
        generatorRegistry.put("randomOffsetDateTime", randomOffsetDateTimeTemplate());
        generatorRegistry.put("randomOffsetTime", randomOffsetTimeTemplate());
        generatorRegistry.put("randomZonedDateTime", randomZonedDateTimeTemplate());
        generatorRegistry.put("randomByteArray", randomByteArrayTemplate());
        generatorRegistry.put("now", nowTemplate());
        generatorRegistry.put("today", todayTemplate());
        generatorRegistry.put("loremIpsum", loremIpsumTemplate());
        generatorRegistry.put("sequence", sequenceTemplate());
        generatorRegistry.put("valueSelector", valueSelectorTemplate());
        generatorRegistry.put("enumSelector", enumSelectorTemplate());
    }

    private static GeneratorTemplate randomStringTemplate() {
        return (args) -> {
            String randomString = parseString(args, 0, "randomString");
            return randomString(randomString);
        };
    }

    private static GeneratorTemplate randomIntTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomInt");
            return randomInt(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomShortTemplate() {
        return (args) -> {
            Pair<Short, Short> range = parseShortRange(args, "randomShort");
            return randomShort(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomByteTemplate() {
        return (args) -> {
            Pair<Byte, Byte> range = parseByteRange(args, "randomByte");
            return randomByte(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomCharTemplate() {
        return (args) -> {
            Pair<Character, Character> range = parseCharRange(args, "randomChar");
            return randomCharacter(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomLongTemplate() {
        return (args) -> {
            Pair<Long, Long> range = parseLongRange(args, "randomLong");
            return randomLong(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomDoubleTemplate() {
        return (args) -> {
            Pair<Double, Double> range = parseDoubleRange(args, "randomDouble");
            return randomDouble(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomFloatTemplate() {
        return (args) -> {
            Pair<Float, Float> range = parseFloatRange(args, "randomFloat");
            return randomFloat(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomBigIntegerTemplate() {
        return (args) -> {
            Pair<Long, Long> range = parseLongRange(args, "randomBigInteger");
            return randomBigInteger(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomBigDecimalTemplate() {
        return (args) -> {
            Pair<Double, Double> range = parseDoubleRange(args, "randomBigDecimal");
            return randomBigDecimal(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomBooleanTemplate() {
        return (args) -> randomBoolean();
    }

    private static GeneratorTemplate randomLocalDateTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomLocalDate");
            return randomLocalDate(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomLocalDateTimeTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomLocalDateTime");
            return randomLocalDateTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomOffsetDateTimeTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomOffsetDateTime");
            return randomOffsetDateTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomOffsetTimeTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomOffsetTime");
            return randomOffsetTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomZonedDateTimeTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomZonedDateTime");
            return randomZonedDateTime(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate randomByteArrayTemplate() {
        return (args) -> {
            Pair<Integer, Integer> range = parseIntRange(args, "randomByteArray");
            return randomByteArray(range.getLeft(), range.getRight());
        };
    }

    private static GeneratorTemplate nowTemplate() {
        return (args) -> now();
    }

    private static GeneratorTemplate todayTemplate() {
        return (args) -> today();
    }

    private static GeneratorTemplate loremIpsumTemplate() {
        return (args) -> {
            int minSize = parseInt(args, 0, "loremIpsum");
            if (args.length == 1) {
                return loremIpsum(minSize);
            }
            int maxSize = parseInt(args, 1, "loremIpsum");
            return loremIpsum(minSize, maxSize);
        };
    }

    private static GeneratorTemplate sequenceTemplate() {
        return (args) -> {
            int start = parseInt(args, 0, "sequence");
            int step = parseInt(args, 1, "sequence");
            return sequence(start, step);
        };
    }

    private static GeneratorTemplate valueSelectorTemplate() {
        return (args) -> {
            boolean allowDuplicates = parseBoolean(args, 0, "valueSelector");
            String[] values = parseVarargs(args, 1);
            return valueSelector(allowDuplicates, values);
        };
    }

    private static GeneratorTemplate enumSelectorTemplate() {
        return (args) -> {
            Class<? extends Enum> enumClass = parseEnum(args, 0, "enumSelector");
            boolean allowDuplicates = parseBoolean(args, 1, "enumSelector");
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

}
