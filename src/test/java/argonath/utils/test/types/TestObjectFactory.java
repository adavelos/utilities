package argonath.utils.test.types;

import argonath.reflector.config.Configuration;
import argonath.reflector.factory.ObjectFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestObjectFactory {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    public void testObjectFactory() {

        Collection<?> collection = ObjectFactory.create(Collection.class);
        assertTrue(collection instanceof ArrayList);

        List<?> list = ObjectFactory.create(List.class);
        assertTrue(list instanceof ArrayList);

        Set<?> set = ObjectFactory.create(Set.class);
        assertTrue(set instanceof HashSet);

        SortedSet<?> sortedSet = ObjectFactory.create(SortedSet.class);
        assertTrue(sortedSet instanceof TreeSet);

        Map<?, ?> map = ObjectFactory.create(Map.class);
        assertTrue(map instanceof HashMap);

        SortedMap<?, ?> sortedMap = ObjectFactory.create(SortedMap.class);
        assertTrue(sortedMap instanceof TreeMap);

        // test create array
        Object array = ObjectFactory.createArray(int[].class);
        assertTrue(array instanceof int[]);
    }
}

