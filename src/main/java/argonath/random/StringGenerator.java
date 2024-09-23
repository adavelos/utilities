package argonath.random;

import argonath.utils.Assert;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class StringGenerator {

    static final Pattern RANGE_FRACTION_MATCH = Pattern.compile("n(\\d+).(\\d+)");

    static final Pattern VARIABLE_RANGE_FRACTION_MATCH = Pattern.compile("n(\\d+)\\.\\.(\\d+).(\\d+)");

    static final Pattern RANGE_MATCH = Pattern.compile("(a|n|an)\\.\\.(\\d+)");

    static final Pattern VARIABLE_RANGE_MATCH = Pattern.compile("(a|n|an)(\\d+)\\.\\.(\\d+)");

    static final Pattern EXACT_MATCH = Pattern.compile("(a|n|an)(\\d+)");

    public static String randomStringUppercase(String... formats) {
        return randomString(formats).toUpperCase();
    }

    public static String randomString(String... formats) {
        return randomString(Arrays.asList(formats));
    }

    public static String randomString(List<String> formats) {
        StringBuilder buf = new StringBuilder();
        formats.forEach(format -> buf.append(generateString(format).toUpperCase()));
        return buf.toString();
    }

    public static Long randomLong(String format) {
        String str = generateString(format);
        Long ret;
        try {
            ret = Long.valueOf(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Format:" + format, e);
        }
        return ret;
    }

    public static BigDecimal randomBigDecimal(String format) {
        String str = randomString(format);
        return new BigDecimal(str);
    }

    public static byte[] randomBinary(String format) {
        String str = randomString(format);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        return bytes;
    }

    public static List<String> randomStringList(int size, String... formats) {
        return IntStream.range(0, size).boxed().map(item -> randomString(formats)).toList();
    }

    private static String generateString(String format) {

        String ret;

        Matcher rangeFractionMatcher = RANGE_FRACTION_MATCH.matcher(format);
        Matcher variableRangeFractionMatcher = VARIABLE_RANGE_FRACTION_MATCH.matcher(format);

        Integer minDigits;
        Integer maxDigits;
        Integer fractionDigits;
        String fmt;

        try {
            if (rangeFractionMatcher.matches()) {
                // handle fraction
                minDigits = maxDigits = Integer.parseInt(rangeFractionMatcher.group(1));
                fractionDigits = Integer.parseInt(rangeFractionMatcher.group(2));
                fmt = "n";
            } else if (variableRangeFractionMatcher.matches()) {
                // handle fraction
                minDigits = Integer.parseInt(variableRangeFractionMatcher.group(1));
                maxDigits = Integer.parseInt(variableRangeFractionMatcher.group(2));
                fractionDigits = Integer.parseInt(variableRangeFractionMatcher.group(3));
                fmt = "n";
            } else {
                Matcher rangeMatcher = RANGE_MATCH.matcher(format);
                if (rangeMatcher.matches()) {
                    // handle range
                    maxDigits = Integer.parseInt(rangeMatcher.group(2));
                    minDigits = 1;
                    fractionDigits = 0;
                    fmt = rangeMatcher.group(1);
                } else {
                    Matcher exactMatcher = EXACT_MATCH.matcher(format);
                    if (exactMatcher.matches()) {
                        maxDigits = minDigits = Integer.parseInt(exactMatcher.group(2));
                        fractionDigits = 0;
                        fmt = exactMatcher.group(1);
                    } else {
                        Matcher variableRangeMatcher = VARIABLE_RANGE_MATCH.matcher(format);
                        if (variableRangeMatcher.matches()) {
                            // handle range
                            maxDigits = Integer.parseInt(variableRangeMatcher.group(3));
                            minDigits = Integer.parseInt(variableRangeMatcher.group(2));
                            fractionDigits = 0;
                            fmt = variableRangeMatcher.group(1);
                        } else {
                            return format;
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid Format:" + format);
        }

        if (fmt.contains("a") && format.contains("n")) {
            ret = generateAlphanumeric(minDigits, maxDigits);
        } else if (fmt.contains("a")) {
            ret = generateAlpha(minDigits, maxDigits);
        } else if (fmt.contains("n")) {
            ret = generateNumeric(minDigits, maxDigits, fractionDigits);
        } else {
            throw new IllegalArgumentException("Invalid Format - Cannot Parse" + format);
        }

        return ret;

    }

    private static String generateNumeric(Integer minDigits, Integer maxDigits, Integer fractionDigits) {
        boolean hasFraction = (fractionDigits > 0);
        Assert.isTrue(maxDigits > fractionDigits, "Invalid format: fraction digits must be less or equal to integral part digits");

        String integral = generateString(minDigits, maxDigits, FormatType.NUMERIC);
        String ret = integral;
        if (hasFraction) {
            String decimal = generateString(0, fractionDigits, FormatType.NUMERIC);
            ret = ret + "." + decimal;
        }

        return ret;
    }

    private static String generateAlpha(Integer minDigits, Integer maxDigits) {
        return generateString(minDigits, maxDigits, FormatType.ALPHA);
    }

    private static String generateAlphanumeric(Integer minDigits, Integer maxDigits) {
        return generateString(minDigits, maxDigits, FormatType.ALPHANUMERIC);
    }

    private static String generateString(Integer minDigits, Integer maxDigits, FormatType formatType) {
        Assert.isTrue(maxDigits >= minDigits, "MAX Digits must be greater or equal than MIN Digits");
        StringBuilder buf = new StringBuilder();
        Integer length = RandomNumber.getInteger(minDigits, maxDigits + 1);
        IntStream.range(0, length).forEach(id -> buf.append(generateChar(formatType)));
        return buf.toString();

    }

    private static char generateChar(FormatType numeric) {
        switch (numeric) {
            case ALPHA:
                return randomChar(ALPHA_SET);
            case NUMERIC:
                return randomChar(NUMERIC_SET);
            case ALPHANUMERIC:
                return randomChar(ALPHANUMERIC_SET);
        }
        throw new IllegalArgumentException("Invalid Format: Type" + numeric);
    }

    private static char randomChar(String alphaSet) {
        Integer index = RandomNumber.getInteger(0, alphaSet.length());
        return alphaSet.charAt(index);
    }

    static final String ALPHA_SET = "abcdefghijklmnopqrstuvwxyz";

    static final String NUMERIC_SET = "0123456789";

    static final String ALPHANUMERIC_SET = ALPHA_SET + NUMERIC_SET;

    enum FormatType {
        ALPHANUMERIC,
        ALPHA,
        NUMERIC;
    }
}
