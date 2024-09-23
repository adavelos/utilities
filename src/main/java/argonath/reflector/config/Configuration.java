package argonath.reflector.config;

import argonath.utils.ConfigurationLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps reflector configurable settigns
 */
public class Configuration {
    private Configuration() {
    }

    // Property Names: must start with 'reflector.' prefix
    private static final String PROP_DATE_FORMAT = "reflector.date.format";
    private static final String PROP_DATE_TIME_FORMAT = "reflector.date.time.format";

    private static final String PROP_OFFSET_DATE_TIME_FORMAT = "reflector.offset.date.time.format";
    private static final String PROP_OFFSET_TIME_FORMAT = "reflector.offset.time.format";

    private static final String PROP_ZONED_DATE_TIME_FORMAT = "reflector.zoned.date.time.format";

    private static final String REFLECTOR_PROPS_FILENAME = "reflector.properties";
    private static Map<String, String> properties = new HashMap<>();

    static {
        properties = ConfigurationLoader.load(REFLECTOR_PROPS_FILENAME);
    }

    public static void withConfigFile(String filename) {
        properties = ConfigurationLoader.load(filename);
    }

    public static void withConfigProperty(String property, String value) {
        properties.put(property, value);
    }

    public static String getProperty(String key) {
        return properties.get(key);
    }

    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value == null ? defaultValue : value;
    }

    public static void setProperty(String key, String value) {
        properties.put(key, value);
    }

    // SPECIFIC CONFIGURATION GETTERS

    public static String dateFormat(String defaultDateFormat) {
        return getProperty(PROP_DATE_FORMAT, defaultDateFormat);
    }

    public static String dateTimeFormat(String defaultDateTimeFormat) {
        return getProperty(PROP_DATE_TIME_FORMAT, defaultDateTimeFormat);
    }

    public static String offsetDateTimeFormat(String defaultOffsetDateTimeFormat) {
        return getProperty(PROP_OFFSET_DATE_TIME_FORMAT, defaultOffsetDateTimeFormat);
    }

    public static String offsetTimeFormat(String defaultOffsetTimeFormat) {
        return getProperty(PROP_OFFSET_TIME_FORMAT, defaultOffsetTimeFormat);
    }

    public static String zonedDateTimeFormat(String defaultZonedDateTimeFormat) {
        return getProperty(PROP_ZONED_DATE_TIME_FORMAT, defaultZonedDateTimeFormat);
    }

    // LENIENT vs. STRICT Mode

    public static boolean isLenient() {
        return Boolean.parseBoolean(getProperty("reflector.lenient", "false"));
    }

    public static void setLenient() {
        setProperty("reflector.generator.lenient", "true");
    }

}
