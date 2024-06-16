package argonath.utils.test;

import argonath.utils.reflector.generator.GeneratorConfig;
import argonath.utils.reflector.generator.ObjectGenerator;
import org.junit.jupiter.api.Test;

public class TestObjectGenerator {

    @Test
    public void testGenerate() {
        TestClass generate = ObjectGenerator.config(TestClass.class)
                .generate();

    }

    public static class TestClass {
        String name;
        int age;
        boolean isConfigured;
    }
}
