package argonath.utils.test.reader;

import argonath.reflector.config.Configuration;
import argonath.reflector.reader.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TestObjectReaderDebug {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testGetObject() {

        TestClass testClass = new TestClass();
        testClass.otherList.add("test");

        List otherList = ObjectReader.get(testClass, "/otherList", List.class);
        Assertions.assertTrue(otherList instanceof LinkedList);


    }

    public static class TestClass {

        private List<String> otherList;


        public TestClass() {
            this.otherList = new LinkedList<>();
        }
    }

}
