package argonath.reflector.generator;

import argonath.reflector.generator.model.GeneratorTemplate;
import argonath.utils.Assert;

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
        // String
        generatorRegistry.put("randomString", (args) -> randomString(parseString(args, 0, "randomString")));

        // Number
        generatorRegistry.put("randomInt", (args) -> randomInt(parseInt(args, 0, "randomInt"), parseInt(args, 1, "randomInt")));
        generatorRegistry.put("randomShort", (args) -> randomShort(parseShort(args, 0, "randomShort"), parseShort(args, 1, "randomShort")));
        generatorRegistry.put("randomByte", (args) -> randomByte(parseByte(args, 0, "randomByte"), parseByte(args, 1, "randomByte")));
        generatorRegistry.put("randomCharacter", (args) -> randomCharacter(parseChar(args, 0, "randomCharacter"), parseChar(args, 1, "randomCharacter")));
        generatorRegistry.put("randomLong", (args) -> randomLong(parseLong(args, 0, "randomLong"), parseLong(args, 1, "randomLong")));
        generatorRegistry.put("randomDouble", (args) -> randomDouble(parseDouble(args, 0, "randomDouble"), parseDouble(args, 1, "randomDouble")));
        generatorRegistry.put("randomFloat", (args) -> randomFloat(parseFloat(args, 0, "randomFloat"), parseFloat(args, 1, "randomFloat")));

        // Boolean
        generatorRegistry.put("randomBoolean", (args) -> randomBoolean());

        // Dates
        generatorRegistry.put("randomLocalDate", (args) -> randomLocalDate(parseInt(args, 0, "randomLocalDate"), parseInt(args, 1, "randomLocalDate")));
        generatorRegistry.put("randomLocalDateTime", (args) -> randomLocalDateTime(parseInt(args, 0, "randomLocalDateTime"), parseInt(args, 1, "randomLocalDateTime")));
        generatorRegistry.put("randomOffsetDateTime", (args) -> randomOffsetDateTime(parseInt(args, 0, "randomOffsetDateTime"), parseInt(args, 1, "randomOffsetDateTime")));
        generatorRegistry.put("randomOffsetTime", (args) -> randomOffsetTime(parseInt(args, 0, "randomOffsetTime"), parseInt(args, 1, "randomOffsetTime")));
        generatorRegistry.put("randomZonedDateTime", (args) -> randomZonedDateTime(parseInt(args, 0, "randomZonedDateTime"), parseInt(args, 1, "randomZonedDateTime")));

        generatorRegistry.put("randomByte", (args) -> randomByteArray(parseInt(args, 0, "randomByteArray"), parseInt(args, 1, "randomByteArray")));

        generatorRegistry.put("now", (args) -> now());
        generatorRegistry.put("today", (args) -> today());
        generatorRegistry.put("loremIpsum", (args) -> {
            int minSize = parseInt(args, 0, "loremIpsum");
            if (args.length == 1) {
                return loremIpsum(minSize);
            }
            int maxSize = parseInt(args, 1, "loremIpsum");
            return loremIpsum(minSize, maxSize);
        });
        generatorRegistry.put("sequence", (args) -> sequence(parseInt(args, 0, "sequence"), parseInt(args, 1, "sequence")));
        generatorRegistry.put("valueSelector", (args) -> valueSelector(parseBoolean(args, 0, "valueSelector"), parseVarargs(args, 1)));
        generatorRegistry.put("enumSelector", (args) -> enumValueSelector(parseEnum(args, 0, "enumSelector"), parseBoolean(args, 1, "enumSelector")));
    }

    private static String parseString(String[] args, int index, String generatorName) {
        if (args.length <= index) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires at least " + (index + 1) + " arguments");
        }
        return args[index];
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

    private static short parseShort(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Short.parseShort(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a short argument at position " + (index + 1));
        }
    }

    private static byte parseByte(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Byte.parseByte(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a byte argument at position " + (index + 1));
        }
    }

    private static char parseChar(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);
        Assert.isTrue(strVal.length() == 1, "Generator " + generatorName + " requires a single character argument at position " + (index + 1) + " but got: " + strVal);
        return strVal.charAt(0);
    }

    private static long parseLong(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Long.parseLong(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a long argument at position " + (index + 1));
        }
    }

    private static double parseDouble(String[] args, int index, String generatorName) {
        String strVal = parseString(args, index, generatorName);

        try {
            return Double.parseDouble(strVal);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Generator " + generatorName + " requires a double argument at position " + (index + 1));
        }
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
