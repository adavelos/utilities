package argonath.utils.test;

import argonath.utils.Assert;
import argonath.utils.reflection.Accessor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestReflectionAccessor {

    @Test
    public void testAccessor() {

        Assert.isTrue(Accessor.isCollection(List.class), "List is a collection");
        Assert.isTrue(!Accessor.isCollection(String.class), "String is not a collection");
        Assert.isTrue(!Accessor.isCollection("alex"), "String literal is not a collection");
        Assert.isTrue(Accessor.isCollection(List.of(1, 2)), "List instance is a collection");

        Assert.isTrue(Accessor.isArrayOrCollection(List.class), "List is a collection");
        Assert.isTrue(Accessor.isArrayOrCollection(new int[]{}), "int[] is an array");
        Assert.isTrue(Accessor.isArrayOrCollection(new Integer[]{}), "Integer[] is an array");
        Assert.isTrue(Accessor.isArrayOrCollection(new String[]{}), "String[] is an array");
        Assert.isTrue(!Accessor.isArrayOrCollection(String.class), "String is not an array or collection");
        Assert.isTrue(!Accessor.isArrayOrCollection("test"), "String literal is not an array or collection");
        Assert.isTrue(Accessor.isArrayOrCollection(List.of(1, 2)), "List instance is an array or collection");

        // test isList
        Assert.isTrue(Accessor.isList(List.class), "List is a List");
        Assert.isTrue(Accessor.isList(List.of(1, 2)), "List instance is a List");
        Assert.isTrue(!Accessor.isList(Collection.class), "List class is not a List");
        Assert.isTrue(!Accessor.isList(Map.of("a", 1, "b", 2)), "Map instance is not a List");

        // test isMap
        Assert.isTrue(Accessor.isMap(Map.class), "Map is a Map");
        Assert.isTrue(Accessor.isMap(Map.of("a", 1, "b", 2)), "Map instance is a Map");
        Assert.isTrue(!Accessor.isMap(List.class), "List class is not a Map");
        Assert.isTrue(!Accessor.isMap(List.of(1, 2)), "List instance is not a Map");

        // test getFields
        List<Field> fields = Accessor.getFields(TestClass.class);
        Assert.isTrue(fields.size() == 4, "TestClass has 4 fields");
        Assert.isTrue(fields.stream().anyMatch(f -> f.getName().equals("name")), "TestClass has field 'name'");
        Assert.isTrue(fields.stream().anyMatch(f -> f.getName().equals("age")), "TestClass has field 'age'");
        Assert.isTrue(fields.stream().anyMatch(f -> f.getName().equals("friends")), "TestClass has field 'friends'");
        Assert.isTrue(fields.stream().anyMatch(f -> f.getName().equals("data")), "TestClass has field 'data'");
        fields = Accessor.getFields(TestClass2.class);
        Assert.isTrue(fields.size() == 0, "TestClass2 has no fields");

        // test getField
        Field field = Accessor.getField("name", TestClass.class);
        Assert.notNull(field, "Field 'name' exists in TestClass");
        field = Accessor.getField("name1", TestClass.class);
        Assert.isTrue(field == null, "Field 'name1' does not exist in TestClass");

        // test Parameter Type
        Class name = Accessor.getGenericType(Accessor.getField("friends", TestClass.class));
        Assert.isTrue(name.equals(String.class), "Generic type of field 'friends' is String");

        // test getArrayOrCollectionValues
        String values = Accessor.getArrayOrCollectionValues(List.of(1, 2, 3)).stream().map(Object::toString).collect(Collectors.joining(","));
        Assert.isTrue(values.equals("1,2,3"), "Values of List(1, 2, 3) are 1, 2, 3");
        values = Accessor.getArrayOrCollectionValues(new Integer[]{1, 2, 3}).stream().map(Object::toString).collect(Collectors.joining(","));
        Assert.isTrue(values.equals("1,2,3"), "Values of int[]{1, 2, 3} are 1, 2, 3");

        // test isSupportedSimpleType
        Assert.isTrue(Accessor.isSupportedSimpleType(String.class), "String is a supported simple type");
        Assert.isTrue(!Accessor.isSupportedSimpleType(TestClass.class), "TestClass is not a supported simple type");

        // test isByteArray
        Field dataField = Accessor.getField("data", TestClass.class);
        Assert.isTrue(Accessor.isByteArray(dataField.getType()), "byte[] is a byte array");

        // test getListSize
        Assert.isTrue(Accessor.getListSize(List.of(1, 2, 3)) == 3, "List(1, 2, 3) has 3 elements");

    }

    public static class TestClass {
        private String name;
        private int age;
        private List<String> friends;
        private byte[] data;
    }

    public static class TestClass2 {
    }
}
