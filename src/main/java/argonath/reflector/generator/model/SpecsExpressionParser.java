package argonath.reflector.generator.model;

import argonath.reflector.generator.GeneratorTemplateRegistry;
import argonath.reflector.generator.Generators;
import argonath.utils.Assert;
import argonath.utils.ConfigurationLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpecsExpressionParser {

    public static Map<String, ObjectSpecs<?>> parseSpecs(String filename) {
        Map<String, String> rawSpecs = ConfigurationLoader.load(filename);

        Map<String, ObjectSpecs<?>> parsedSpecs = new HashMap<>();
        for (Map.Entry<String, String> entry : rawSpecs.entrySet()) {
            String selectorKey = entry.getKey();
            String value = entry.getValue();
            ObjectSpecs<?> specs = parseSpec(selectorKey, value);
            parsedSpecs.put(selectorKey, specs);
        }
        return parsedSpecs;
    }

    public static <T> Pair<String, ObjectSpecs<T>> parseSpec(String value) {
        String[] parts = value.split("=");
        if (parts.length < 1 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid Specs Expression: " + value);
        }
        String selectorKey = parts[0];
        String specsExpression = parts[1];
        ObjectSpecs<T> objectSpecs = parseSpec(selectorKey, specsExpression);
        return Pair.of(selectorKey, objectSpecs);
    }

    public static <T> ObjectSpecs<T> parseSpec(String selectorKey, String value) {
        String line = selectorKey + "=" + value;
        String[] parts = value.split("\\|");
        if (parts.length < 1 || parts.length > 3) {
            throw new IllegalArgumentException("Invalid spec: " + line);
        }

        String optionalityStr = parts[0];

        boolean hasGenerator = (parts.length == 3) || (parts.length == 2 && !parts[1].startsWith("size"));
        boolean hasCardinality = parts.length == 3 || (parts.length == 2 && parts[1].startsWith("size"));

        String cardinalityStr = parts.length == 3 ? parts[1] : hasCardinality ? parts[1] : null;
        String generatorStr = parts.length == 3 ? parts[2] : hasGenerator ? parts[1] : null;

        Optionality optionality = parseOptionality(optionalityStr, line);
        Cardinality cardinality = hasCardinality ? parseCardinality(cardinalityStr, line) : null;
        Generator<?> generatorType = hasGenerator ? parseGenerator(generatorStr, line) : null;

        return new ObjectSpecs(generatorType, optionality, cardinality);
    }

    private static Optionality parseOptionality(String optionalityStr, String line) {
        switch (optionalityStr) {
            case "M":
                return Optionality.MANDATORY;
            case "O":
                return Optionality.OPTIONAL;
            case "N":
                return Optionality.NEVER;
            default:
                throw new IllegalArgumentException("Invalid optionality: " + optionalityStr + " at line: '" + line + "'");
        }
    }

    private static Pattern SIZE_PATTERN = Pattern.compile("size\\((\\d+)(?:,\\s*(\\d+))?\\)");

    private static Cardinality parseCardinality(String cardinalityStr, String line) {
        Matcher matcher = SIZE_PATTERN.matcher(cardinalityStr);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cardinality: " + cardinalityStr + " at line: '" + line + "'");
        }

        if (matcher.group(2) != null) {
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            return new Cardinality(min, max);
        } else if (matcher.group(1) != null) {
            int exact = Integer.parseInt(matcher.group(1));
            return new Cardinality(exact, exact);
        } else {
            throw new IllegalArgumentException("Invalid cardinality: " + cardinalityStr + " at line: '" + line + "'");
        }
    }

    private static Generator<?> parseGenerator(String generatorStr, String line) {
        int openParen = generatorStr.indexOf('(');
        int closeParen = generatorStr.lastIndexOf(')');
        if (openParen == -1 || closeParen == -1 || closeParen <= openParen) {
            return Generators.fixedValue(generatorStr.trim());
        }
        String generatorName = generatorStr.substring(0, openParen);
        Assert.isTrue(generatorName.length() > 0, "Generator name is empty at line: '" + line + "'");
        String argsStr = generatorStr.substring(openParen + 1, closeParen).trim();

        String[] args;
        if (argsStr.isEmpty()) {
            args = new String[0];
        } else {
            args = argsStr.split(",");
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i].trim();
            }
        }
        GeneratorTemplate generatorSupplier = GeneratorTemplateRegistry.generator(generatorName);
        if (generatorSupplier == null) {
            throw new IllegalArgumentException("Unknown generator: " + generatorName + " at line: '" + line + "'");
        }
        try {
            return generatorSupplier.buildGenerator(args);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid generator arguments: " + generatorStr + " at line: '" + line + "'");
        }
    }

}