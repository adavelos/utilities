package argonath.utils.test.generator;

import argonath.reflector.generator.ObjectGenerator;
import argonath.reflector.generator.model.ObjectSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static argonath.reflector.generator.FieldSelector.ofPath;
import static argonath.reflector.generator.Generators.randomInt;
import static argonath.reflector.generator.Generators.randomString;
import static argonath.reflector.generator.model.ObjectSpecs.generator;

public class TestObjectGeneratorApi {
    /*
        - Selectors variances
        - with Specs
        - with Value
        - Code List
        - SEED
        - All Mandatory
     */

    @Test
    public void testWithSpecsWithAllMandatory() {
        Outer obj = ObjectGenerator.create(Outer.class)
                .withAllMandatory()
                .withSpecs(ofPath("/intField"), generator(Integer.class, randomInt(1000, 1010)).mandatory())
                .withSpecs(ofPath("/stringField"), generator(String.class, randomString("an12")).mandatory())
                .withSpecs(ofPath("/intArray"), ObjectSpecs.generator(Integer.class, randomInt(2000, 2010)).size(10, 12))
                .withSpecs(ofPath("/stringArray"), ObjectSpecs.generator(String.class, randomString("an12")).size(20, 24))
                .withSpecs(ofPath("/stringList"), ObjectSpecs.generator(String.class, randomString("an12")).size(30, 34))
                .ignore(ofPath("/toBeIgnored")) // not applicable since withAllMandatory is set

                // inner class
                .withSpecs(ofPath("/inner"), ObjectSpecs.create(Inner.class).mandatory())
                .withSpecs(ofPath("/inner/intField"), generator(Integer.class, randomInt(1000, 1010)))
                .withSpecs(ofPath("/inner/stringField"), generator(String.class, randomString("an12")))
                .withSpecs(ofPath("/inner/intArray"), ObjectSpecs.generator(Integer.class, randomInt(2000, 2010)).mandatory().size(10, 12))
                .withSpecs(ofPath("/inner/stringArray"), ObjectSpecs.generator(String.class, randomString("an12")).mandatory().size(20, 24))
                .withSpecs(ofPath("/inner/stringList"), ObjectSpecs.generator(String.class, randomString("an12")).mandatory().size(30, 34))
                .ignore(ofPath("/inner/intList")) // not applicable since withAllMandatory is set
                .generate();

        // Outer
        Assertions.assertNotNull(obj);
        Assertions.assertTrue(obj.intField >= 1000 && obj.intField < 1010);
        Assertions.assertTrue(obj.stringField.length() == 12);
        Assertions.assertTrue(obj.intArray.length >= 10 && obj.intArray.length < 12);
        Assertions.assertTrue(obj.stringArray.length >= 20 && obj.stringArray.length < 24);
        Assertions.assertTrue(obj.stringList.size() >= 30 && obj.stringList.size() < 34);

        // Inner
        Assertions.assertNotNull(obj.inner);
        Assertions.assertTrue(obj.inner.intField >= 1000 && obj.inner.intField < 1010);
        Assertions.assertTrue(obj.inner.stringField.length() == 12);
        Assertions.assertTrue(obj.inner.intArray.length >= 10 && obj.inner.intArray.length < 12);
        Assertions.assertTrue(obj.inner.stringArray.length >= 20 && obj.inner.stringArray.length < 24);
        Assertions.assertTrue(obj.inner.stringList.size() >= 30 && obj.inner.stringList.size() < 34);

    }

    // Similar but not with all mandatory. before assertions check if object null
    @Test
    public void testWithSpecsWithAllOptional() {
        for (int i = 0; i < 10; i++) {
            Outer obj = ObjectGenerator.create(Outer.class)
                    .withSpecs(ofPath("/intField"), generator(Integer.class, randomInt(1000, 1010)))
                    .withSpecs(ofPath("/stringField"), generator(String.class, randomString("an12")).mandatory())
                    .withSpecs(ofPath("/intArray"), ObjectSpecs.generator(Integer.class, randomInt(2000, 2010)).size(10, 12))
                    .withSpecs(ofPath("/stringArray"), ObjectSpecs.generator(String.class, randomString("an12")).size(20, 24))
                    .withSpecs(ofPath("/stringList"), ObjectSpecs.generator(String.class, randomString("an12")).size(30, 34))
                    .ignore(ofPath("/toBeIgnored")) // not applicable since withAllMandatory is set

                    // inner class
                    .withSpecs(ofPath("/inner"), ObjectSpecs.create(Inner.class))
                    .withSpecs(ofPath("/inner/intField"), generator(Integer.class, randomInt(1000, 1010)))
                    .withSpecs(ofPath("/inner/stringField"), generator(String.class, randomString("an12")))
                    .withSpecs(ofPath("/inner/intArray"), ObjectSpecs.generator(Integer.class, randomInt(2000, 2010)).size(10, 12))
                    .withSpecs(ofPath("/inner/stringArray"), ObjectSpecs.generator(String.class, randomString("an12")).size(20, 24))
                    .withSpecs(ofPath("/inner/stringList"), ObjectSpecs.generator(String.class, randomString("an12")).size(30, 34))
                    .ignore(ofPath("/innerNone"))
                    .generate();

            // Outer
            Assertions.assertNotNull(obj);

            if (obj != null) {
                Assertions.assertNotNull(obj.intField);
                Assertions.assertTrue(obj.intField >= 1000 && obj.intField < 1010);

                //explicitly set to mandatory
                Assertions.assertTrue(obj.stringField.length() == 12);

                if (obj.intArray != null) {
                    Assertions.assertTrue(obj.intArray.length >= 10 && obj.intArray.length < 12);
                }
                if (obj.stringArray != null) {
                    Assertions.assertTrue(obj.stringArray.length >= 20 && obj.stringArray.length < 24);
                }
                if (obj.stringList != null) {
                    Assertions.assertTrue(obj.stringList.size() >= 30 && obj.stringList.size() < 34);
                }
                Assertions.assertNull(obj.toBeIgnored);
                Assertions.assertNull(obj.innerNone);

                if (obj.inner != null) {
                    Assertions.assertNotNull(obj.inner.intField);
                    Assertions.assertTrue(obj.inner.intField >= 1000 && obj.inner.intField < 1010);
                    if (obj.inner.stringField != null) {
                        Assertions.assertTrue(obj.inner.stringField.length() == 12);
                    }
                    if (obj.inner.intArray != null) {
                        Assertions.assertTrue(obj.inner.intArray.length >= 10 && obj.inner.intArray.length < 12);
                    }
                    if (obj.inner.stringArray != null) {
                        Assertions.assertTrue(obj.inner.stringArray.length >= 20 && obj.inner.stringArray.length < 24);
                    }
                    if (obj.inner.stringList != null) {
                        Assertions.assertTrue(obj.inner.stringList.size() >= 30 && obj.inner.stringList.size() < 34);
                    }
                }
            }
        }
    }

    @Test
    public void testWithValues() {
        Outer obj = ObjectGenerator.create(Outer.class)
                .withValue(ofPath("/intField"), 1005)
                .withValue(ofPath("/stringField"), "abcdefghijkl")
                .withValue(ofPath("/intArray"), new int[]{2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010})
                .withValue(ofPath("/stringArray"), new String[]{"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18", "a19", "a20"})
                .withValue(ofPath("/stringList"), Arrays.asList("b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8", "b9", "b10", "b11", "b12", "b13", "b14", "b15", "b16", "b17", "b18", "b19", "b20", "b21", "b22", "b23", "b24", "b25", "b26", "b27", "b28", "b29", "b30"))
                .ignore(ofPath("/toBeIgnored"))

                // inner class
                .withValue(ofPath("/inner/intField"), 1008)
                .withValue(ofPath("/inner/stringField"), "mnopqrstuvwx")
                .withValue(ofPath("/inner/intArray"), new int[]{2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014})
                .withValue(ofPath("/inner/stringArray"), new String[]{"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19", "c20"})
                .withValue(ofPath("/inner/stringList"), Arrays.asList("d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "d11", "d12", "d13", "d14", "d15", "d16", "d17", "d18", "d19", "d20", "d21", "d22", "d23", "d24", "d25", "d26", "d27", "d28", "d29", "d30"))
                .generate();

        // Assertions using JUnit Jupiter Assertions API
        Assertions.assertNotNull(obj);
        Assertions.assertEquals(1005, obj.intField);
        Assertions.assertEquals("abcdefghijkl", obj.stringField);
        Assertions.assertEquals(10, obj.intArray.length);
        Assertions.assertEquals(20, obj.stringArray.length);
        Assertions.assertEquals(30, obj.stringList.size());
        Assertions.assertNull(obj.toBeIgnored);

        Assertions.assertNotNull(obj.inner);
        Assertions.assertEquals(1008, obj.inner.intField);
        Assertions.assertEquals("mnopqrstuvwx", obj.inner.stringField);
        Assertions.assertEquals(10, obj.inner.intArray.length);
        Assertions.assertEquals(20, obj.inner.stringArray.length);
        Assertions.assertEquals(30, obj.inner.stringList.size());

    }

    @Test
    public void testWithValues2() {

        Inner inner = ObjectGenerator.create(Inner.class)
                .withValue(ofPath("/intField"), 1008)
                .withValue(ofPath("/stringField"), "mnopqrstuvwx")
                .withValue(ofPath("/intArray"), new int[]{2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014})
                .withValue(ofPath("/stringArray"), new String[]{"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "c11", "c12", "c13", "c14", "c15", "c16", "c17", "c18", "c19", "c20"})
                .withValue(ofPath("/stringList"), Arrays.asList("d1", "d2", "d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "d11", "d12", "d13", "d14", "d15", "d16", "d17", "d18", "d19", "d20", "d21", "d22", "d23", "d24", "d25", "d26", "d27", "d28", "d29", "d30"))
                .withValue(ofPath("/intList"), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
                .generate();

        Outer obj = ObjectGenerator.create(Outer.class)
                .withValue(ofPath("/intField"), 1005)
                .withValue(ofPath("/stringField"), "abcdefghijkl")
                .withValue(ofPath("/intArray"), new int[]{2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010})
                .withValue(ofPath("/stringArray"), new String[]{"a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "a10", "a11", "a12", "a13", "a14", "a15", "a16", "a17", "a18", "a19", "a20"})
                .withValue(ofPath("/stringList"), Arrays.asList("b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8", "b9", "b10", "b11", "b12", "b13", "b14", "b15", "b16", "b17", "b18", "b19", "b20", "b21", "b22", "b23", "b24", "b25", "b26", "b27", "b28", "b29", "b30"))
                .ignore(ofPath("/toBeIgnored"))
                .withValue(ofPath("/inner"), inner)
                .generate();


        // Assertions using JUnit Jupiter Assertions API
        Assertions.assertNotNull(obj);
        Assertions.assertEquals(1005, obj.intField);
        Assertions.assertEquals("abcdefghijkl", obj.stringField);
        Assertions.assertEquals(10, obj.intArray.length);
        Assertions.assertEquals(20, obj.stringArray.length);
        Assertions.assertEquals(30, obj.stringList.size());
        Assertions.assertNull(obj.toBeIgnored);

        Assertions.assertNotNull(obj.inner);
        Assertions.assertEquals(1008, obj.inner.intField);
        Assertions.assertEquals("mnopqrstuvwx", obj.inner.stringField);
        Assertions.assertEquals(10, obj.inner.intArray.length);
        Assertions.assertEquals(20, obj.inner.stringArray.length);
        Assertions.assertEquals(30, obj.inner.stringList.size());

    }

    @Test
    public void testValueOverridesSpecs() {
        Outer obj = ObjectGenerator.create(Outer.class)
                .withSpecs(ofPath("/intField"), generator(Integer.class, randomInt(90, 100)))
                .withValue(ofPath("/intField"), 50)
                .withSpecs(ofPath("/stringField"), generator(String.class, randomString("an12")).mandatory())
                .withValue(ofPath("/stringField"), "test")
                // inner class
                .withSpecs(ofPath("/inner/intField"), generator(Integer.class, randomInt(60, 70)))
                .withValue(ofPath("/inner/intField"), 30)
                .withSpecs(ofPath("/inner/stringField"), generator(String.class, randomString("an12")).mandatory())
                .withValue(ofPath("/inner/stringField"), "test2")
                .generate();

        // Assertions using JUnit Jupiter Assertions API
        Assertions.assertNotNull(obj);
        Assertions.assertEquals(50, obj.intField);
        Assertions.assertEquals("test", obj.stringField);

        Assertions.assertNotNull(obj.inner);
        Assertions.assertEquals(30, obj.inner.intField);
        Assertions.assertEquals("test2", obj.inner.stringField);

    }

    private static class Outer {
        int intField;
        String stringField;
        int[] intArray;
        String[] stringArray;
        List<String> stringList;
        Map<String, String> stringMap;
        List<Integer> intList;
        Map<Integer, Integer> intMap;
        String toBeIgnored;

        Inner inner;
        Inner innerNone;
        List<Inner> innerList;
        Map<String, Inner> innerMap;
    }

    private static class Inner {
        int intField;
        String stringField;
        int[] intArray;
        String[] stringArray;
        List<String> stringList;
        List<Integer> intList;
    }

}
