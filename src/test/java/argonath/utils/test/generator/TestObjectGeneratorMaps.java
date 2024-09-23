package argonath.utils.test.generator;

import argonath.reflector.generator.ObjectGenerator;
import argonath.reflector.generator.model.ObjectSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static argonath.reflector.generator.FieldSelector.ofPath;
import static argonath.reflector.generator.Generators.randomInt;
import static argonath.reflector.generator.Generators.randomString;
import static argonath.reflector.generator.model.ObjectSpecs.generator;

@Disabled
class TestObjectGeneratorMaps {

    @Test
    void testMapGenerators() {
        Outer obj = ObjectGenerator.create(Outer.class)
                .withAllMandatory()
                .withSpecs(ofPath("/intMap"), ObjectSpecs.generator(Integer.class, randomInt(20,30)).size(3, 3))
                .withSpecs(ofPath("/intStringMap"), ObjectSpecs.generator(String.class, randomString("an10")).size(3, 3))
                .withSpecs(ofPath("/stringMap"), ObjectSpecs.generator(String.class, randomString("an10")).size(3, 3))
                .withSpecs(ofPath("/stringIntMap"), ObjectSpecs.generator(Integer.class, randomInt(1000, 1010)).size(3, 3))
                .withSpecs(ofPath("/innerMap"), ObjectSpecs.create(Inner.class))
                .withSpecs(ofPath("/innerMap/intField"), generator(Integer.class, randomInt(1000, 1010)).size(3, 3))
                .withSpecs(ofPath("/innerMap/stringField"), generator(String.class, randomString("an10")).size(3, 3))
                .generate();

        Assertions.assertNotNull(obj.intMap);
        Assertions.assertEquals(3, obj.intMap.size());
        obj.intMap.forEach((k, v) -> {
            Assertions.assertTrue(k instanceof Integer);
            Assertions.assertTrue(v instanceof Integer);
        });

        Assertions.assertNotNull(obj.intStringMap);
        Assertions.assertEquals(3, obj.intStringMap.size());
        obj.intStringMap.forEach((k, v) -> {
            Assertions.assertTrue(k instanceof Integer);
            Assertions.assertTrue(v instanceof String);
        });

        Assertions.assertNotNull(obj.stringMap);
        Assertions.assertEquals(3, obj.stringMap.size());
        obj.stringMap.forEach((k, v) -> {
            Assertions.assertTrue(k instanceof String);
            Assertions.assertTrue(v instanceof String);
        });

        Assertions.assertNotNull(obj.stringIntMap);
        Assertions.assertEquals(3, obj.stringIntMap.size());
        obj.stringIntMap.forEach((k, v) -> {
            Assertions.assertTrue(k instanceof String);
            Assertions.assertTrue(v instanceof Integer);
        });

    }




    private static class Outer {

        Map<Integer, Integer> intMap;
        Map<Integer, String> intStringMap;
        Map<String, String> stringMap;
        Map<String, Integer> stringIntMap;

        Map<String, Inner> innerMap;
    }

    private static class Inner {
        int intField;
        String stringField;
    }

}
