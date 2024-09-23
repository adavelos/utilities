package argonath.utils;

import argonath.reflector.config.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigurationLoader {
    private ConfigurationLoader() {
    }

    public static Map<String, String> load(String filename) {
        Map<String, String> configMap = new HashMap<>();
        Properties prop = new Properties();
        try (InputStream input = Configuration.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                return configMap;
            }

            // Load properties from the input stream
            prop.load(input);

            for (String key : prop.stringPropertyNames()) {
                configMap.put(key, prop.getProperty(key));
            }

        } catch (IOException ex) {
            throw new IllegalArgumentException("Error loading configuration file: " + filename, ex);
        }

        return configMap;
    }

}
