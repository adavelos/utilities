package argonath.utils.test.utils;

import argonath.reflector.config.Configuration;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.types.simple.SimpleTypes;
import argonath.utils.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class TestReflectionAccessor {
    @BeforeAll
    static void setup() {
        Configuration.withConfigFile("reflector.properties");
    }

    @Test
    void testAccessor() {

        Assertions.assertTrue(ReflectiveAccessor.isCollection(List.class), "List is a collection");
        Assertions.assertFalse(ReflectiveAccessor.isCollection(String.class), "String is not a collection");
        Assertions.assertFalse(ReflectiveAccessor.isCollection("alex"), "String literal is not a collection");
        Assertions.assertTrue(ReflectiveAccessor.isCollection(List.of(1, 2)), "List instance is a collection");

        Assertions.assertTrue(ReflectiveAccessor.isArrayOrCollection(List.class), "List is a collection");
        Assertions.assertTrue(ReflectiveAccessor.isArrayOrCollection(new int[]{}), "int[] is an array");
        Assertions.assertTrue(ReflectiveAccessor.isArrayOrCollection(new Integer[]{}), "Integer[] is an array");
        Assertions.assertTrue(ReflectiveAccessor.isArrayOrCollection(new String[]{}), "String[] is an array");
        Assertions.assertFalse(ReflectiveAccessor.isArrayOrCollection(String.class), "String is not an array or collection");
        Assertions.assertFalse(ReflectiveAccessor.isArrayOrCollection("test"), "String literal is not an array or collection");
        Assertions.assertTrue(ReflectiveAccessor.isArrayOrCollection(List.of(1, 2)), "List instance is an array or collection");

        // test isList
        Assertions.assertTrue(ReflectiveAccessor.isList(List.class), "List is a List");
        Assertions.assertTrue(ReflectiveAccessor.isList(List.of(1, 2)), "List instance is a List");
        Assertions.assertFalse(ReflectiveAccessor.isList(Collection.class), "List class is not a List");
        Assertions.assertFalse(ReflectiveAccessor.isList(Map.of("a", 1, "b", 2)), "Map instance is not a List");

        // test isMap
        Assertions.assertTrue(ReflectiveAccessor.isMap(Map.class), "Map is a Map");
        Assertions.assertTrue(ReflectiveAccessor.isMap(Map.of("a", 1, "b", 2)), "Map instance is a Map");
        Assertions.assertFalse(ReflectiveAccessor.isMap(List.class), "List class is not a Map");
        Assertions.assertFalse(ReflectiveAccessor.isMap(List.of(1, 2)), "List instance is not a Map");

        // test getFields
        List<Field> fields = ReflectiveAccessor.getFields(TestClass.class);
        Assertions.assertEquals(4, fields.size(), "TestClass has 4 fields");
        Assertions.assertTrue(fields.stream().anyMatch(f -> f.getName().equals("name")), "TestClass has field 'name'");
        Assertions.assertTrue(fields.stream().anyMatch(f -> f.getName().equals("age")), "TestClass has field 'age'");
        Assertions.assertTrue(fields.stream().anyMatch(f -> f.getName().equals("friends")), "TestClass has field 'friends'");
        Assertions.assertTrue(fields.stream().anyMatch(f -> f.getName().equals("data")), "TestClass has field 'data'");
        fields = ReflectiveAccessor.getFields(TestClass2.class);
        Assertions.assertEquals(0, fields.size(), "TestClass2 has no fields");

        // test getField
        Field field = ReflectiveAccessor.getField("name", TestClass.class);
        Assert.notNull(field, "Field 'name' exists in TestClass");
        field = ReflectiveAccessor.getField("name1", TestClass.class);
        Assertions.assertNull(field, "Field 'name1' does not exist in TestClass");

        // test Parameter Type
        Class<?> name = ReflectiveAccessor.getGenericType(ReflectiveAccessor.getField("friends", TestClass.class));
        Assertions.assertEquals(name, String.class, "Generic type of field 'friends' is String");

        // test getArrayOrCollectionValues
        List<?> arrayOrCollectionValues = ReflectiveAccessor.getArrayOrCollectionValues(List.of(1, 2, 3));
        String values = arrayOrCollectionValues.stream().map(Object::toString).collect(Collectors.joining(","));
        Assertions.assertEquals("1,2,3", values, "Values of List(1, 2, 3) are 1, 2, 3");
        List<?> arrayOrCollectionValues2 = ReflectiveAccessor.getArrayOrCollectionValues(new Integer[]{1, 2, 3});
        values = arrayOrCollectionValues2.stream().map(Object::toString).collect(Collectors.joining(","));
        Assertions.assertEquals("1,2,3", values, "Values of int[]{1, 2, 3} are 1, 2, 3");

        // test isSupportedSimpleType
        Assertions.assertTrue(SimpleTypes.isSimpleType(String.class), "String is a supported final type");
        Assertions.assertFalse(SimpleTypes.isSimpleType(TestClass.class), "TestClass is not a supported final type");

        // test isByteArray
        Field dataField = ReflectiveAccessor.getField("data", TestClass.class);
        Assertions.assertTrue(ReflectiveAccessor.isByteArray(dataField.getType()), "byte[] is a byte array");

        // test getListSize
        Assertions.assertEquals(3, (int) ReflectiveAccessor.getListSize(List.of(1, 2, 3)), "List(1, 2, 3) has 3 elements");

        // test compatible List types
        List<String> list = List.of("a", "b", "c");
        Assertions.assertTrue(ReflectiveAccessor.isListOfCompatibleType(list, String.class), "List<String> is compatible with String");

        // test that a list of ExtendedClass is compatible to a list of TestClass
        List<TestClass> extendedList = List.of(new TestClass(), new TestClass());
        Assertions.assertTrue(ReflectiveAccessor.isListOfCompatibleType(extendedList, ExtendedClass.class), "List<TestClass> is compatible with ExtendedClass");

        // test isEnum
        Assertions.assertTrue(ReflectiveAccessor.isEnum(TestEnum.class), "TestEnum");
    }

    public static class TestClass {
        private String name;
        private int age;
        private List<String> friends;
        private byte[] data;
    }

    public static class TestClass2 {
    }

    public static class ExtendedClass extends TestClass {
    }

    public static enum TestEnum {
        A, B, C
    }
}
