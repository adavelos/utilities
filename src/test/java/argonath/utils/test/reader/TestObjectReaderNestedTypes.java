package argonath.utils.test.reader;

import argonath.reflector.config.Configuration;
import argonath.reflector.reader.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class TestObjectReaderNestedTypes {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testNestedTypesReader() {
        TestClass testObject = new TestClass();
        testObject.mapWithListSimple = Map.of(
                "key1", List.of("value1", "value2"),
                "key2", List.of("value3", "value4"));
        testObject.mapWithListComplex = Map.of(
                "key1", List.of(new InnerTestClass("value1"), new InnerTestClass("value2")),
                "key2", List.of(new InnerTestClass("value3"), new InnerTestClass("value4")));
        testObject.mapWithMapSimple = Map.of("key1", Map.of(
                "key1", "value1", "key2", "value2"),
                "key2", Map.of("key3", "value3", "key4", "value4"));
        testObject.mapWithMapComplex = Map.of(
                "key1", Map.of("key1", new InnerTestClass("value1"), "key2", new InnerTestClass("value2")),
                "key2", Map.of("key3", new InnerTestClass("value3"), "key4", new InnerTestClass("value4")));

        // /mapWithListSimple
        Object o1 = ObjectReader.get(testObject, "/mapWithListSimple");
        Assertions.assertTrue(o1 instanceof Map && ((Map<?, ?>) o1).size() == 2);
        // /mapWithListSimple.key
        Object o2 = ObjectReader.get(testObject, "/mapWithListSimple.key");
        Assertions.assertTrue(o2 instanceof List && ((List<?>) o2).size() == 2);
        // /mapWithListSimple.key[0]
        Object o3 = ObjectReader.get(testObject, "/mapWithListSimple[key1]");
        Assertions.assertTrue(o3 instanceof List && ((List<?>) o3).size() == 2);
        // /mapWithListSimple.value
        Object o4 = ObjectReader.get(testObject, "/mapWithListSimple.value");
        Assertions.assertTrue(o4 instanceof List && ((List<?>) o4).size() == 2);


    }

    public static class TestClass {
        private Map<String, List<String>> mapWithListSimple;
        private Map<String, List<InnerTestClass>> mapWithListComplex;
        private Map<String, Map<String, String>> mapWithMapSimple;
        private Map<String, Map<String, InnerTestClass>> mapWithMapComplex;

    }

    public static class InnerTestClass {
        String field;

        public InnerTestClass(String field) {
            this.field = field;
        }
    }


}
