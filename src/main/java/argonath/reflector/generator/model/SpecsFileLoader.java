package argonath.reflector.generator.model;

import argonath.reflector.generator.GeneratorTemplateRegistry;
import argonath.utils.Assert;
import argonath.utils.ConfigurationLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Specs Expression Language (Specs EL) is a simple language to define object specs in a concise way from an external file.
 * A record in the file is a key-value pair, where the key is a path in the object graph (corresponds to a Field Selector by Path) and the Value is the Specs EL definition.
 * <p>
 * The specs EL is a string that can have the following format:
 * - there can be 2 or 3 parts, separated by '|'
 * - the first part is the optionality (M|O|N)
 * - if there is a third part (multi-value field), it is the generator type and the second part is the cardinality
 * - if there is no third part, the second part is the generator type (singe-value field - cardinality is zero)
 * - if there is no second part, there is no generator (single-value complex type or simple type populated by default generator)
 * <p>
 * The cardinality has the format 'size(min, max)' or 'size(exact)':
 * - size(3) - exact cardinality of 3
 * - size(1, 3) - min cardinality of 1 and max cardinality of 3
 * <p>
 * The generator type is a string that has the following format:
 * - the generator name
 * - the arguments in parentheses, separated by commas
 * <p>
 * Examples of generators:
 * - randomString(10) - random string of length 10
 * - randomInt(1, 10) - random integer between 1 and 10
 * - randomLocalDate(-10, 10) - random local date between -10 and 10 days from now
 * - randomLocalDateTime(-100, -50) - random local date time between -100 and -50 days from now
 * - randomOffsetDateTime(0, 10) - random offset date time between 0 and 10 days from now
 * - randomOffsetTime(0, 10) - random offset time between 0 and 10 hours from now
 * - randomZonedDateTime(0, 10) - random zoned date time between 0 and 10 days from now
 * - randomBoolean() - random boolean
 * - now() - current date and time
 * - today() - current date
 * - loremIpsum(100) - lorem ipsum text of 10 characters
 * - loremIpsum(100, 500) - lorem ipsum text of 100 to 500 characters
 * - sequence(1, 1) - sequence of integers starting from 1 and incrementing by 1
 * - valueSelector(true, "A", "B", "C") - random value selector from the given set with replacement
 * - valueSelector(false, "A", "B", "C") - random value selector from the given set without replacement
 * - enumSelector(MyEnum.class, false) - random enum selector from the given enum without replacement
 * <p>
 * Example of full line specs:
 * - /path/to/field=M|randomString(10)
 * - /path/to/listOfIntegersField=O|size(1, 3)|randomInt(1, 10)
 * - /path/to/listOfStringsField=M|size(1, 3)|randomString(10)
 * - /path/to/complexType=O
 */
public class SpecsFileLoader {

    /**
     * Loads the specs from the given file and parses them into a map of object specs
     */
    public static Map<String, ObjectSpecs> parseSpecs(String filename) {
        Map<String, String> rawSpecs = ConfigurationLoader.load(filename);
        Map<String, ObjectSpecs> parsedSpecs = rawSpecs.entrySet().stream()
                .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), parseSpec(entry.getValue())), HashMap::putAll);
        return parsedSpecs;
    }

    /**
     * Parses an individual specs definition (EL) into an ObjectSpecs object
     */
    public static ObjectSpecs parseSpec(String value) {

        /*
            The rules are the following:
            - there can be 2 or 3 parts, separated by '|'
            - the first part is the optionality (M|O|N)
            - if there is a third part (multi-value field), it is the generator type and the second part is the cardinality
            - if there is no third part, the second part is the generator type (singe-value field)
            - if there is no second part, there is no generator (single value complex type or default generator will be used)
         */

        String[] parts = value.split("\\|");
        if (parts.length < 1 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid spec: " + value);
        }

        String optionalityStr = parts[0];
        String cardinalityStr = parts.length == 3 ? parts[1] : null;
        String generatorStr = parts.length == 3 ? parts[2] : parts[1];

        Optionality optionality = parseOptionality(optionalityStr);
        Cardinality cardinality = cardinalityStr == null ? null : parseCardinality(cardinalityStr);
        Generator<?> generatorType = generatorStr == null ? null : parseGenerator(generatorStr);

        return new ObjectSpecs(generatorType, optionality, cardinality);
    }

    private static Optionality parseOptionality(String optionalityStr) {
        switch (optionalityStr) {
            case "M":
                return Optionality.MANDATORY;
            case "O":
                return Optionality.OPTIONAL;
            case "N":
                return Optionality.NEVER;
            default:
                throw new IllegalArgumentException("Invalid optionality: " + optionalityStr);
        }
    }

    private static Pattern SIZE_PATTERN = Pattern.compile("size\\((\\d+)(?:,\\s*(\\d+))?\\)");

    private static Cardinality parseCardinality(String cardinalityStr) {
        Matcher matcher = SIZE_PATTERN.matcher(cardinalityStr);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cardinality: " + cardinalityStr);
        }

        if (matcher.group(2) != null) {
            // Two numbers format: size(min, max)
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            return new Cardinality(min, max);
        } else if (matcher.group(1) != null) {
            // Single number format: size(exact)
            int exact = Integer.parseInt(matcher.group(1));
            return new Cardinality(exact, exact);
        } else {
            throw new IllegalArgumentException("Invalid cardinality: " + cardinalityStr);
        }
    }

    private static Generator<?> parseGenerator(String generatorStr) {
        /*
            The generator string has the following format:
            - the generator name
            - the arguments in parentheses, separated by commas
         */

        int openParen = generatorStr.indexOf('(');
        int closeParen = generatorStr.lastIndexOf(')');
        if (openParen == -1 || closeParen == -1 || closeParen <= openParen) {
            throw new IllegalArgumentException("Invalid generator syntax: " + generatorStr);
        }
        String generatorName = generatorStr.substring(0, openParen);
        Assert.isTrue(generatorName.length() > 0, "Generator name is empty");
        String argsStr = generatorStr.substring(openParen + 1, closeParen);
        String[] args = argsStr.split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        GeneratorTemplate generatorSupplier = GeneratorTemplateRegistry.generator(generatorName);
        if (generatorSupplier == null) {
            throw new IllegalArgumentException("Unknown generator: " + generatorName);
        }
        return generatorSupplier.buildGenerator(args);
    }
}
