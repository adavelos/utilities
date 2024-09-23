package argonath.utils.test.reader;

import argonath.reflector.config.Configuration;
import argonath.reflector.reader.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TestObjectReader {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    void testGetObject() {

        InnerClass innerClass = new InnerClass("innerString", 1, 2L, 3.0f, 4.0, true, new BigDecimal(5), new BigInteger("6"), List.of("innerFriend1", "innerFriend2"), new byte[]{1, 2, 3});
        List<InnerClass> innerClasses = List.of(
                new InnerClass("innerString1", 1, 2L, 3.0f, 4.0, true, new BigDecimal(5), new BigInteger("6"), List.of("innerFriend1", "innerFriend2"), new byte[]{1, 2, 3}),
                new InnerClass("innerString2", 1, 2L, 3.0f, 4.0, true, new BigDecimal(5), new BigInteger("6"), List.of("innerFriend1", "innerFriend2"), new byte[]{1, 2, 3}),
                new InnerClass("innerString3", 1, 2L, 3.0f, 4.0, true, new BigDecimal(5), new BigInteger("6"), List.of("innerFriend1", "innerFriend2"), new byte[]{1, 2, 3})
        );
        TestClass inputObject = new TestClass("string", 1, 2L, 3.0f, 4.0, true, new BigDecimal(5), new BigInteger("6"), List.of("friend1", "friend2"), new byte[]{1, 2, 3},
                innerClass, innerClasses);
        inputObject.otherList = new LinkedList<>(List.of("test1"));

        // Root Level
        TestClass testObject = ObjectReader.get(inputObject, "/", TestClass.class);
        Assertions.assertNotNull(testObject, "testObject is not null");
        Assertions.assertEquals(inputObject.stringField, testObject.stringField, "name is equal");
        Assertions.assertEquals(inputObject.integerField, testObject.integerField, "age is equal");
        Assertions.assertEquals(inputObject.longField, testObject.longField, "age is equal");
        Assertions.assertEquals(inputObject.floatField, testObject.floatField, "age is equal");
        Assertions.assertEquals(inputObject.doubleField, testObject.doubleField, "age is equal");
        Assertions.assertEquals(inputObject.booleanField, testObject.booleanField, "age is equal");
        Assertions.assertEquals(inputObject.bigDecimalField, testObject.bigDecimalField, "age is equal");
        Assertions.assertEquals(inputObject.bigIntegerField, testObject.bigIntegerField, "age is equal");
        Assertions.assertArrayEquals(inputObject.friends.toArray(), testObject.friends.toArray(), "friends are equal");
        Assertions.assertArrayEquals(inputObject.data, testObject.data, "data is equal");
        Assertions.assertNotNull(testObject.innerClass, "innerClass is not null");
        Assertions.assertArrayEquals(inputObject.innerClasses.toArray(), testObject.innerClasses.toArray(), "innerClasses are equal");

        // Test Class Fields
        String testObjectName = ObjectReader.get(inputObject, "/stringField", String.class);
        Assertions.assertEquals(inputObject.stringField, testObjectName, "name is equal");
        Integer testObjectAge = ObjectReader.get(inputObject, "/integerField", Integer.class);
        Assertions.assertEquals(inputObject.integerField, testObjectAge, "age is equal");
        Long testObjectLong = ObjectReader.get(inputObject, "/longField", Long.class);
        Assertions.assertEquals(inputObject.longField, testObjectLong, "age is equal");
        Float testObjectFloat = ObjectReader.get(inputObject, "/floatField", Float.class);
        Assertions.assertEquals(inputObject.floatField, testObjectFloat, "age is equal");
        Double testObjectDouble = ObjectReader.get(inputObject, "/doubleField", Double.class);
        Assertions.assertEquals(inputObject.doubleField, testObjectDouble, "age is equal");
        Boolean testObjectBoolean = ObjectReader.get(inputObject, "/booleanField", Boolean.class);
        Assertions.assertEquals(inputObject.booleanField, testObjectBoolean, "age is equal");
        BigDecimal testObjectBigDecimal = ObjectReader.get(inputObject, "/bigDecimalField", BigDecimal.class);
        Assertions.assertEquals(inputObject.bigDecimalField, testObjectBigDecimal, "age is equal");
        BigInteger testObjectBigInteger = ObjectReader.get(inputObject, "/bigIntegerField", BigInteger.class);
        Assertions.assertEquals(inputObject.bigIntegerField, testObjectBigInteger, "age is equal");
        Collection<String> testObjectFriends = ObjectReader.list(inputObject, "/friends", String.class);
        Assertions.assertArrayEquals(inputObject.friends.toArray(), testObjectFriends.toArray(), "friends are equal");
        byte[] testObjectData = ObjectReader.get(inputObject, "/data", byte[].class);
        Assertions.assertArrayEquals(inputObject.data, testObjectData, "data is equal");
        InnerClass testObjectInner = ObjectReader.get(inputObject, "/innerClass", InnerClass.class);
        Assertions.assertNotNull(testObjectInner, "inner is not null");
        Collection<InnerClass> testObjectInnerClasses = ObjectReader.list(inputObject, "/innerClasses", InnerClass.class);
        Assertions.assertArrayEquals(inputObject.innerClasses.toArray(), testObjectInnerClasses.toArray(), "innerClasses are equal");
        List<InnerClass> testObjectInnerClasses2 = ObjectReader.get(inputObject, "/innerClasses", List.class);
        Assertions.assertArrayEquals(inputObject.innerClasses.toArray(), testObjectInnerClasses2.toArray(), "innerClasses are equal");

//        // Test Inner Class
        InnerClass inner = ObjectReader.get(inputObject, "/innerClass", InnerClass.class);
        Assertions.assertNotNull(inner, "inner is not null");
        Assertions.assertEquals(innerClass.stringField, inner.stringField, "name is equal");
        Assertions.assertEquals(innerClass.integerField, inner.integerField, "age is equal");
        Assertions.assertEquals(innerClass.longField, inner.longField, "age is equal");
        Assertions.assertEquals(innerClass.floatField, inner.floatField, "age is equal");
        Assertions.assertEquals(innerClass.doubleField, inner.doubleField, "age is equal");
        Assertions.assertEquals(innerClass.booleanField, inner.booleanField, "age is equal");
        Assertions.assertEquals(innerClass.bigDecimalField, inner.bigDecimalField, "age is equal");
        Assertions.assertEquals(innerClass.bigIntegerField, inner.bigIntegerField, "age is equal");
        Assertions.assertArrayEquals(innerClass.friends.toArray(), inner.friends.toArray(), "friends are equal");
        Assertions.assertArrayEquals(innerClass.data, inner.data, "data is equal");

        // Test Inner Class Fields
        String innerName = ObjectReader.get(inputObject, "/innerClass/stringField", String.class);
        Assertions.assertEquals(innerClass.stringField, innerName, "name is equal");
        Integer innerAge = ObjectReader.get(inputObject, "/innerClass/integerField", Integer.class);
        Assertions.assertEquals(innerClass.integerField, innerAge, "age is equal");
        Long innerLong = ObjectReader.get(inputObject, "/innerClass/longField", Long.class);
        Assertions.assertEquals(innerClass.longField, innerLong, "age is equal");
        Float innerFloat = ObjectReader.get(inputObject, "/innerClass/floatField", Float.class);
        Assertions.assertEquals(innerClass.floatField, innerFloat, "age is equal");
        Double innerDouble = ObjectReader.get(inputObject, "/innerClass/doubleField", Double.class);
        Assertions.assertEquals(innerClass.doubleField, innerDouble, "age is equal");
        Boolean innerBoolean = ObjectReader.get(inputObject, "/innerClass/booleanField", Boolean.class);
        Assertions.assertEquals(innerClass.booleanField, innerBoolean, "age is equal");
        BigDecimal innerBigDecimal = ObjectReader.get(inputObject, "/innerClass/bigDecimalField", BigDecimal.class);
        Assertions.assertEquals(innerClass.bigDecimalField, innerBigDecimal, "age is equal");
        BigInteger innerBigInteger = ObjectReader.get(inputObject, "/innerClass/bigIntegerField", BigInteger.class);
        Assertions.assertEquals(innerClass.bigIntegerField, innerBigInteger, "age is equal");
        Collection<String> innerFriends = ObjectReader.list(inputObject, "/innerClass/friends", String.class);
        Assertions.assertArrayEquals(innerClass.friends.toArray(), innerFriends.toArray(), "friends are equal");
        byte[] innerData = ObjectReader.get(inputObject, "/innerClass/data", byte[].class);
        Assertions.assertArrayEquals(innerClass.data, innerData, "data is equal");

        // Test Inner Class list
        Collection<InnerClass> innerClassesList = ObjectReader.list(inputObject, "/innerClasses", InnerClass.class);
        Assertions.assertNotNull(innerClassesList, "innerClasses is not null");
        Assertions.assertEquals(innerClasses.size(), innerClassesList.size(), "innerClasses size is equal");
        Assertions.assertArrayEquals(innerClasses.toArray(), innerClassesList.toArray(), "innerClasses are equal");

        // Non-existing field
        Assertions.assertThrows(IllegalArgumentException.class, () -> ObjectReader.get(inputObject, "/nonExistingField", String.class));
        // Null Field
        Assertions.assertNull(ObjectReader.get(inputObject, "/nullField", String.class));
        // Null List: must return empty list
        Collection<String> nullList = ObjectReader.list(inputObject, "/nullList", String.class);
        Assertions.assertNotNull(nullList, "nullList is not null");
        Assertions.assertEquals(0, nullList.size(), "nullList size is 0");

        List otherList = ObjectReader.get(inputObject, "/otherList", List.class);
        Assertions.assertTrue(otherList instanceof LinkedList);

        Set setInner = ObjectReader.get(inputObject, "/setInner", Set.class);
        Assertions.assertTrue(setInner instanceof HashSet);

        Set innerSet = ObjectReader.get(inputObject, "/innerClasses[0]/innerSet", Set.class);
        Assertions.assertTrue(innerSet instanceof HashSet);

        List<String> innertSets = ObjectReader.list(inputObject, "/innerClasses/innerSet", String.class);
        Assertions.assertTrue(innertSets.stream().allMatch(s -> s instanceof String));


    }

    public static class TestClass {
        private String stringField;
        private String nullField;
        private Integer integerField;
        private Long longField;
        private Float floatField;
        private Double doubleField;
        private Boolean booleanField;
        private BigDecimal bigDecimalField;
        private BigInteger bigIntegerField;

        private List<String> friends;
        private List<String> nullList;

        private List<String> otherList;

        private byte[] data;
        private InnerClass innerClass;

        private List<InnerClass> innerClasses;
        private List<InnerClass> innerClassesLinkedList;

        private Set<InnerClass> setInner;

        public TestClass(String stringField, Integer integerField, Long longField, Float floatField, Double doubleField, Boolean booleanField, BigDecimal bigDecimalField, BigInteger bigIntegerField,
                         List<String> friends, byte[] data, InnerClass innerClass,
                         List<InnerClass> innerClasses) {
            this.stringField = stringField;
            this.integerField = integerField;
            this.longField = longField;
            this.floatField = floatField;
            this.doubleField = doubleField;
            this.booleanField = booleanField;
            this.bigDecimalField = bigDecimalField;
            this.bigIntegerField = bigIntegerField;
            this.friends = friends;
            this.data = data;
            this.innerClass = innerClass;
            this.innerClasses = innerClasses;
            this.innerClassesLinkedList = new LinkedList<>(innerClasses);
            this.otherList = new LinkedList<>();
            this.setInner = new HashSet<>();
        }
    }

    public static class InnerClass {
        private String stringField;
        private Integer integerField;
        private Long longField;
        private Float floatField;
        private Double doubleField;
        private Boolean booleanField;
        private BigDecimal bigDecimalField;
        private BigInteger bigIntegerField;

        private List<String> friends;
        private byte[] data;

        private Set<String> innerSet;

        public InnerClass(String stringField, Integer integerField, Long longField, Float floatField, Double doubleField, Boolean booleanField, BigDecimal bigDecimalField, BigInteger bigIntegerField, List<String> friends, byte[] data) {
            this.stringField = stringField;
            this.integerField = integerField;
            this.longField = longField;
            this.floatField = floatField;
            this.doubleField = doubleField;
            this.booleanField = booleanField;
            this.bigDecimalField = bigDecimalField;
            this.bigIntegerField = bigIntegerField;
            this.friends = friends;
            this.data = data;
            this.innerSet = new HashSet<>(friends);
        }

        // for array comparisons
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            InnerClass that = (InnerClass) o;
            return Objects.equals(stringField, that.stringField) &&
                    Objects.equals(integerField, that.integerField) &&
                    Objects.equals(longField, that.longField) &&
                    Objects.equals(floatField, that.floatField) &&
                    Objects.equals(doubleField, that.doubleField) &&
                    Objects.equals(booleanField, that.booleanField) &&
                    Objects.equals(bigDecimalField, that.bigDecimalField) &&
                    Objects.equals(bigIntegerField, that.bigIntegerField) &&
                    Objects.equals(friends, that.friends) &&
                    Arrays.equals(data, that.data);
        }
    }
}
