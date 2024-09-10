package argonath.utils.test.reader;

import argonath.reflector.config.Configuration;
import argonath.reflector.reader.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TestObjectReaderValueMapper {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testValueMapper() {
        TestClass testObject = new TestClass();
        testObject.simpleMap = Map.of("key1", "value1", "key2", "value2");

        Object o1 = ObjectReader.get(testObject, "/simpleMap");
        Assertions.assertTrue(o1 instanceof Map);
        Map<String, String> map = (Map<String, String>) o1;
        Assertions.assertTrue(map.containsKey("key1"));
        Assertions.assertTrue(map.containsKey("key2"));
        Assertions.assertEquals("value1", map.get("key1"));
        Assertions.assertEquals("value2", map.get("key2"));

        Object o2 = ObjectReader.get(testObject, "/simpleMap[key1]");
        Assertions.assertEquals("value1", o2);
        List<String> l2 = ObjectReader.list(testObject, "/simpleMap[key1]", String.class);
        Assertions.assertEquals(List.of("value1"), l2);

        Object o3 = ObjectReader.get(testObject, "/simpleMap[invalid]");
        // assert that o3 is an empty list
        Assertions.assertTrue(o3 instanceof List && ((List<?>) o3).isEmpty());
        List<String> l3 = ObjectReader.list(testObject, "/simpleMap[invalid]", String.class);
        Assertions.assertTrue(l3.isEmpty());

        Object o4 = ObjectReader.get(testObject, "/simpleMap.key");
        Collections.sort((List<String>) o4);
        Assertions.assertEquals(List.of("key1", "key2"), o4);
        List<String> l4 = ObjectReader.list(testObject, "/simpleMap.key", String.class);
        Collections.sort(l4);
        Assertions.assertEquals(List.of("key1", "key2"), l4);

        Object o5 = ObjectReader.get(testObject, "/simpleMap.value");
        Collections.sort((List<String>) o5);
        Assertions.assertEquals(List.of("value1", "value2"), o5);
        List<String> l5 = ObjectReader.list(testObject, "/simpleMap.value", String.class);
        Collections.sort(l5);
        Assertions.assertEquals(List.of("value1", "value2"), l5);

        // Complex Map
        testObject.complexMap = Map.of("key1", new InnerTestClass("value1"), "key2", new InnerTestClass("value2"));

        Object o6 = ObjectReader.get(testObject, "/complexMap");
        Assertions.assertTrue(o6 instanceof Map);

        Object o7 = ObjectReader.get(testObject, "/complexMap[key1]");
        Assertions.assertTrue(o7 instanceof InnerTestClass);
        Assertions.assertEquals("value1", ((InnerTestClass) o7).field);
        List<InnerTestClass> l7 = ObjectReader.list(testObject, "/complexMap[key1]", InnerTestClass.class);
        Assertions.assertEquals(1, l7.size());
        Assertions.assertEquals("value1", l7.get(0).field);

        Object o8 = ObjectReader.get(testObject, "/complexMap.key");
        Collections.sort((List<String>) o8);
        Assertions.assertEquals(List.of("key1", "key2"), o8);
        List<String> l8 = ObjectReader.list(testObject, "/complexMap.key", String.class);
        Collections.sort(l8);
        Assertions.assertEquals(List.of("key1", "key2"), l8);

        Object o9 = ObjectReader.get(testObject, "/complexMap.value");
        List<InnerTestClass> l9 = (List<InnerTestClass>) o9;
        Set<String> s9 = l9.stream().map(i -> i.field).collect(Collectors.toSet());
        Assertions.assertEquals(Set.of("value1", "value2"), s9);
        List<InnerTestClass> l9_1 = ObjectReader.list(testObject, "/complexMap.value", InnerTestClass.class);
        Set<String> s9_1 = l9_1.stream().map(i -> i.field).collect(Collectors.toSet());
        Assertions.assertEquals(Set.of("value1", "value2"), s9_1);

        Object o10 = ObjectReader.get(testObject, "/complexMap[key1]/field");
        Assertions.assertEquals("value1", o10);
        List<String> l10 = ObjectReader.list(testObject, "/complexMap[key1]/field", String.class);
        Assertions.assertEquals(List.of("value1"), l10);

        Object o11 = ObjectReader.get(testObject, "/complexMap.value/field");
        List<String> l11 = (List<String>) o11;
        Set<String> s11 = l11.stream().collect(Collectors.toSet());
        Assertions.assertEquals(Set.of("value1", "value2"), s11);

        String o12 = ObjectReader.get(testObject, "/complexMap[key1]/field", String.class);
        Assertions.assertEquals("value1", o12);

        // assert that an intuitive message is used in case of misuse of the casting class
        try {
            ObjectReader.get(testObject, "/complexMap.value/field", String.class);
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException && e.getMessage().contains("Non-iterable result requested"));
        }


    }

    public static class TestClass {
        private Map<String, String> simpleMap;
        private Map<String, InnerTestClass> complexMap;

    }

    public static class InnerTestClass {
        String field;

        public InnerTestClass(String field) {
            this.field = field;
        }
    }


}
