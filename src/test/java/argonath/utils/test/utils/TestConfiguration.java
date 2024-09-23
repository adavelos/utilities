package argonath.utils.test.utils;

import argonath.reflector.config.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestConfiguration {

    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("reflector.properties");
    }

    @Test
    void testConfigurationFile() {
        // default configuration file
        String testValue = Configuration.getProperty("test.property");
        Assertions.assertEquals("default", testValue, "Default configuration file is loaded");

        // empty configuration file
        Configuration.withConfigFile("default.reflector.properties");
        testValue = Configuration.getProperty("test.property");
        Assertions.assertNull(testValue, "Empty configuration file is loaded");

        // alternative configuration file
        Configuration.withConfigFile("alt.reflector.properties");
        testValue = Configuration.getProperty("test.property");
        Assertions.assertEquals("alternative", testValue, "Alternative configuration file is loaded");
    }

    @Test
    void testInvalidConfigurationFile() {
        Configuration.withConfigFile("invalid.reflector.properties");
        String testValue = Configuration.getProperty("test.property");
        Assertions.assertNull(testValue, "Invalid configuration file is loaded: no failure, but configuration is empty");
    }
}
