package argonath.utils.test.reader;

import argonath.reflector.config.Configuration;
import argonath.reflector.reader.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TestObjectReaderValueFilter {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    void testSingleFilter() {
        InnerClass innerClass1 = new InnerClass("1", "innerName1");
        InnerClass innerClass2 = new InnerClass("2", "innerName2");
        InnerClass innerClass3 = new InnerClass("3", "innerName3");
        InnerClass innerClass4 = new InnerClass("4", "innerName4");
        InnerClass innerClass5 = new InnerClass("5", "innerName5");

        TestClass inputObject = new TestClass("1",
                List.of("value1", "value2", "value3", "value4", "value5"),
                Set.of("value1", "value2", "value3", "value4", "value5"),
                Map.of("key1", "value1", "key2", "value2", "key3", "value3", "key4", "value4", "key5", "value5"),
                List.of(innerClass1, innerClass2, innerClass3, innerClass4, innerClass5),
                Set.of(innerClass1, innerClass2, innerClass3, innerClass4, innerClass5),
                Map.of("key1", innerClass1, "key2", innerClass2, "key3", innerClass3, "key4", innerClass4, "key5", innerClass5)
        );

        testSimpleList(inputObject);
        testSimpleArray(inputObject);
        testSimpleSet(inputObject);
        testSimpleMap(inputObject);

        testComplexList(inputObject);
        testComplexArray(inputObject);

    }

    private void testSimpleList(TestClass inputObject) {
        // Simple Test for 'id'
        Object id = ObjectReader.get(inputObject, "id");
        Assertions.assertEquals("1", id);

        Object list1 = ObjectReader.get(inputObject, "simpleList[1]");
        Assertions.assertEquals("value2", list1);
        List<String> list1_2 = ObjectReader.list(inputObject, "simpleList[1]", String.class);
        Assertions.assertEquals(List.of("value2"), list1_2);

        List<String> list2to5 = ObjectReader.get(inputObject, "simpleList[2:4]", List.class);
        Assertions.assertEquals(List.of("value3", "value4", "value5"), list2to5);
        List<String> list2to5_2 = ObjectReader.list(inputObject, "simpleList[2:4]", String.class);
        Assertions.assertEquals(List.of("value3", "value4", "value5"), list2to5_2);

        List<String> list0to3 = ObjectReader.get(inputObject, "simpleList[0:2]", List.class);
        Assertions.assertEquals(List.of("value1", "value2", "value3"), list0to3);
        List<String> list0to3_2 = ObjectReader.list(inputObject, "simpleList[0:2]", String.class);
        Assertions.assertEquals(List.of("value1", "value2", "value3"), list0to3_2);

        List<String> list3toEnd = ObjectReader.get(inputObject, "simpleList[3:]", List.class);
        Assertions.assertEquals(List.of("value4", "value5"), list3toEnd);
        List<String> list3toEnd_2 = ObjectReader.list(inputObject, "simpleList[3:]", String.class);
        Assertions.assertEquals(List.of("value4", "value5"), list3toEnd_2);

        List<String> list1_3_4 = ObjectReader.get(inputObject, "simpleList[1,3,4]", List.class);
        Assertions.assertEquals(List.of("value2", "value4", "value5"), list1_3_4);
        List<String> list1_3_4_2 = ObjectReader.list(inputObject, "simpleList[1,3,4]", String.class);
        Assertions.assertEquals(List.of("value2", "value4", "value5"), list1_3_4_2);

        String listValue = ObjectReader.get(inputObject, "simpleList[value1]", String.class);
        Assertions.assertEquals("value1", listValue);
        List<String> listValue_2 = ObjectReader.list(inputObject, "simpleList[value1]", String.class);
        Assertions.assertEquals(List.of("value1"), listValue_2);

        // assert that following line throws an exception

        try {
            List<String> invalid1 = ObjectReader.get(inputObject, "simpleList[test]", List.class);
            throw new Exception("Invalid filter test failed");
        } catch (Exception e) {
            //
        }

        try {
            List<String> invalid2 = ObjectReader.get(inputObject, "simpleList[a:b]", List.class);
            throw new Exception("Invalid filter test failed");
        } catch (Exception e) {
            //
        }

        try {
            List<String> invalid3 = ObjectReader.get(inputObject, "simpleList[:2]", List.class);
            throw new Exception("Invalid filter test failed");
        } catch (Exception e) {
            //
        }

        try {
            List<String> invalid4 = ObjectReader.get(inputObject, "simpleList[:]", List.class);
            throw new Exception("Invalid filter test failed");
        } catch (Exception e) {
            //
        }
    }

    private void testSimpleArray(TestClass inputObject) {

        String list1 = ObjectReader.get(inputObject, "simpleArray[1]", String.class);
        Assertions.assertEquals("value2", list1);
        List<String> list1_2 = ObjectReader.list(inputObject, "simpleArray[1]", String.class);
        Assertions.assertEquals(List.of("value2"), list1_2);

        String[] list2to5 = ObjectReader.get(inputObject, "simpleArray[2:4]", String[].class);
        Assertions.assertEquals(List.of("value3", "value4", "value5"), Arrays.asList(list2to5));
        List<String> list2to5_2 = ObjectReader.list(inputObject, "simpleArray[2:4]", String.class);
        Assertions.assertEquals(List.of("value3", "value4", "value5"), list2to5_2);

        String[] list0to3 = ObjectReader.get(inputObject, "simpleArray[0:2]", String[].class);
        Assertions.assertEquals(List.of("value1", "value2", "value3"), Arrays.asList(list0to3));
        List<String> list0to3_2 = ObjectReader.list(inputObject, "simpleArray[0:2]", String.class);
        Assertions.assertEquals(List.of("value1", "value2", "value3"), list0to3_2);

        String[] list3toEnd = ObjectReader.get(inputObject, "simpleArray[3:]", String[].class);
        Assertions.assertEquals(List.of("value4", "value5"), Arrays.asList(list3toEnd));
        List<String> list3toEnd_2 = ObjectReader.list(inputObject, "simpleArray[3:]", String.class);
        Assertions.assertEquals(List.of("value4", "value5"), list3toEnd_2);

        String[] list1_3_4 = ObjectReader.get(inputObject, "simpleArray[1,3,4]", String[].class);
        Assertions.assertEquals(List.of("value2", "value4", "value5"), Arrays.asList(list1_3_4));
        List<String> list1_3_4_2 = ObjectReader.list(inputObject, "simpleArray[1,3,4]", String.class);
        Assertions.assertEquals(List.of("value2", "value4", "value5"), list1_3_4_2);

        String listValue = ObjectReader.get(inputObject, "simpleArray[value1]", String.class);
        Assertions.assertEquals("value1", listValue);
        List<String> listValue_2 = ObjectReader.list(inputObject, "simpleArray[value1]", String.class);
        Assertions.assertEquals(List.of("value1"), listValue_2);


    }

    private void testComplexList(TestClass inputObject) {

        Object list1 = ObjectReader.get(inputObject, "complexList[1]/id");
        Assertions.assertEquals("2", list1);
        List<String> list1_2 = ObjectReader.list(inputObject, "complexList[1]/id", String.class);
        Assertions.assertEquals(List.of("2"), list1_2);

        List<String> list2to5 = ObjectReader.get(inputObject, "complexList[2:4]/id", List.class);
        Assertions.assertEquals(List.of("3", "4", "5"), list2to5);
        List<String> list2to5_2 = ObjectReader.list(inputObject, "complexList[2:4]/id", String.class);
        Assertions.assertEquals(List.of("3", "4", "5"), list2to5_2);

        List<String> list0to3 = ObjectReader.get(inputObject, "complexList[0:2]/id", List.class);
        Assertions.assertEquals(List.of("1", "2", "3"), list0to3);
        List<String> list0to3_2 = ObjectReader.list(inputObject, "complexList[0:2]/id", String.class);
        Assertions.assertEquals(List.of("1", "2", "3"), list0to3_2);

        List<String> list3toEnd = ObjectReader.get(inputObject, "complexList[3:]/id", List.class);
        Assertions.assertEquals(List.of("4", "5"), list3toEnd);
        List<String> list3toEnd_2 = ObjectReader.list(inputObject, "complexList[3:]/id", String.class);
        Assertions.assertEquals(List.of("4", "5"), list3toEnd_2);

    }

    private void testComplexArray(TestClass inputObject) {
        Object list1 = ObjectReader.get(inputObject, "complexArray[1]/id");
        Assertions.assertEquals("2", list1);
        List<String> list1_2 = ObjectReader.list(inputObject, "complexArray[1]/id", String.class);
        Assertions.assertEquals(List.of("2"), list1_2);

        List<String> list2to5 = ObjectReader.get(inputObject, "complexArray[2:4]/id", List.class);
        Assertions.assertEquals(List.of("3", "4", "5"), list2to5);
        List<String> list2to5_2 = ObjectReader.list(inputObject, "complexArray[2:4]/id", String.class);
        Assertions.assertEquals(List.of("3", "4", "5"), list2to5_2);

        List<String> list0to3 = ObjectReader.get(inputObject, "complexArray[0:2]/id", List.class);
        Assertions.assertEquals(List.of("1", "2", "3"), list0to3);
        List<String> list0to3_2 = ObjectReader.list(inputObject, "complexArray[0:2]/id", String.class);
        Assertions.assertEquals(List.of("1", "2", "3"), list0to3_2);

        List<String> list3toEnd = ObjectReader.get(inputObject, "complexArray[3:]/id", List.class);
        Assertions.assertEquals(List.of("4", "5"), list3toEnd);
        List<String> list3toEnd_2 = ObjectReader.list(inputObject, "complexArray[3:]/id", String.class);
        Assertions.assertEquals(List.of("4", "5"), list3toEnd_2);

        List<String> list1_3_4 = ObjectReader.get(inputObject, "complexArray[1,3,4]/id", List.class);
        Assertions.assertEquals(List.of("2", "4", "5"), list1_3_4);
        List<String> list1_3_4_2 = ObjectReader.list(inputObject, "complexArray[1,3,4]/id", String.class);
        Assertions.assertEquals(List.of("2", "4", "5"), list1_3_4_2);
    }

    private void testSimpleSet(TestClass inputObject) {
        Object set1 = ObjectReader.get(inputObject, "simpleSet[value1]");
        Assertions.assertEquals("value1", set1);
        List<String> set1_2 = ObjectReader.list(inputObject, "simpleSet[value1]", String.class);
        Assertions.assertEquals(List.of("value1"), set1_2);

        Object set2 = ObjectReader.get(inputObject, "simpleSet[value1,value2]");
        Assertions.assertEquals(Set.of("value1", "value2"), set2);
        List<String> set2_2 = ObjectReader.list(inputObject, "simpleSet[value1,value2]", String.class);
        ArrayList<String> set2_2_sorted = new ArrayList<>(set2_2);
        Collections.sort(set2_2_sorted);
        Assertions.assertEquals(List.of("value1", "value2"), set2_2_sorted);

        Object setWildcard = ObjectReader.get(inputObject, "simpleSet[value*]");
        Assertions.assertEquals(Set.of("value1", "value2", "value3", "value4", "value5"), setWildcard);
        List<String> setWildcard_2 = ObjectReader.list(inputObject, "simpleSet[value*]", String.class);
        List<String> setWildcard_2_sorted = new ArrayList<>(setWildcard_2);
        Collections.sort(setWildcard_2_sorted);
        Assertions.assertEquals(List.of("value1", "value2", "value3", "value4", "value5"), setWildcard_2_sorted);

        Object setWildcard2 = ObjectReader.get(inputObject, "simpleSet[*alue1]");
        Assertions.assertEquals("value1", setWildcard2);
        List<String> setWildcard2_2 = ObjectReader.list(inputObject, "simpleSet[*alue1]", String.class);
        Assertions.assertEquals(List.of("value1"), setWildcard2_2);

        Object setWildcard3 = ObjectReader.get(inputObject, "simpleSet[*alue*]");
        Assertions.assertEquals(Set.of("value1", "value2", "value3", "value4", "value5"), setWildcard3);
        List<String> setWildcard3_2 = ObjectReader.list(inputObject, "simpleSet[*alue*]", String.class);
        List<String> setWildcard3_2_sorted = new ArrayList<>(setWildcard3_2);
        Collections.sort(setWildcard3_2_sorted);
        Assertions.assertEquals(List.of("value1", "value2", "value3", "value4", "value5"), setWildcard3_2_sorted);

        Object setWildcard4 = ObjectReader.get(inputObject, "simpleSet[*test*]");
        Assertions.assertEquals(Set.of(), setWildcard4);
        List<String> setWildcard4_2 = ObjectReader.list(inputObject, "simpleSet[*test*]", String.class);
        Assertions.assertEquals(List.of(), setWildcard4_2);


        try {
            Object invalidSet1 = ObjectReader.get(inputObject, "simpleSet[value1:value2]");
            throw new Exception("Invalid filter test failed");
        } catch (Exception e) {
            //
        }
    }

    private void testSimpleMap(TestClass inputObject) {
        Object map = ObjectReader.get(inputObject, "simpleMap");
        Assertions.assertEquals(Map.of("key1", "value1", "key2", "value2", "key3", "value3", "key4", "value4", "key5", "value5"), map);

        Object map1 = ObjectReader.get(inputObject, "simpleMap[key1]");
        Assertions.assertEquals("value1", map1);
        List<String> map1_2 = ObjectReader.list(inputObject, "simpleMap[key1]", String.class);
        Assertions.assertEquals(List.of("value1"), map1_2);

        Object map2 = ObjectReader.get(inputObject, "simpleMap[key1,key2]");
        List map2_sorted = new ArrayList((List) map2); // to sort the list (since map is not ordered
        Collections.sort(map2_sorted);
        Assertions.assertEquals(List.of("value1", "value2"), map2_sorted);

        List<String> map2_2 = ObjectReader.list(inputObject, "simpleMap[key1,key2]", String.class);
        List<String> map2_2_sorted = new ArrayList<>(map2_2);
        Collections.sort(map2_2_sorted);
        Assertions.assertEquals(List.of("value1", "value2"), map2_2_sorted);

        Object mapWildcard = ObjectReader.get(inputObject, "simpleMap[key*]");
        List mapWildcard_sorted = new ArrayList((List) mapWildcard);
        Collections.sort(mapWildcard_sorted);
        Assertions.assertEquals(List.of("value1", "value2", "value3", "value4", "value5"), mapWildcard_sorted);
        List<String> mapWildcard_2 = ObjectReader.list(inputObject, "simpleMap[key*]", String.class);
        List<String> mapWildcard_2_sorted = new ArrayList<>(mapWildcard_2);
        Collections.sort(mapWildcard_2_sorted);
        Assertions.assertEquals(List.of("value1", "value2", "value3", "value4", "value5"), mapWildcard_2_sorted);

        try {
            ObjectReader.get(inputObject, "simpleMap[value1:value2]");
            throw new Exception("Invalid filter test failed");
        } catch (Exception e) {
            //
        }

    }

    public static class TestClass {
        private String id;
        private List<String> simpleList;

        private String[] simpleArray;
        private Set<String> simpleSet;
        private Map<String, String> simpleMap;
        private Set<InnerClass> complexSet;
        private List<InnerClass> complexList;

        private InnerClass[] complexArray;
        private Map<String, InnerClass> complexMap;

        public TestClass(String id,
                         List<String> simpleList, Set<String> simpleSet, Map<String, String> simpleMap,
                         List<InnerClass> complexList, Set<InnerClass> complexSet, Map<String, InnerClass> complexMap) {
            this.id = id;
            this.simpleList = simpleList;
            this.simpleSet = simpleSet;
            this.simpleMap = simpleMap;
            this.complexSet = complexSet;
            this.complexList = complexList;
            this.complexMap = complexMap;
            this.simpleArray = simpleList.toArray(new String[0]);
            this.complexArray = complexList.toArray(new InnerClass[0]);
        }
    }

    public static class InnerClass {
        private String id;
        private String name;

        public InnerClass(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof InnerClass) {
                InnerClass other = (InnerClass) obj;
                return this.id.equals(other.id) && this.name.equals(other.name);
            }
            return false;
        }

    }
}
