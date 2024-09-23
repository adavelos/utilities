package argonath.utils.test.types;

import argonath.reflector.config.Configuration;
import argonath.reflector.types.iterable.IterableTypes;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestIterableTypes {
    @BeforeAll
    public static void setup() {
        Configuration.withConfigFile("default.selector.properties");
    }

    @Test
    void testIterableType() {
        assertNotNull(IterableTypes.iterableType(List.class));
        assertNotNull(IterableTypes.iterableType(Set.class));
        assertNull(IterableTypes.iterableType(String.class));
    }

    @Test
    void testIsIterableType() {
        assertTrue(IterableTypes.isIterableType(List.class));
        assertTrue(IterableTypes.isIterableType(Set.class));
        assertFalse(IterableTypes.isIterableType(String.class));
    }

    @Test
    void testAsCollection() {
        Collection<?> collection = IterableTypes.asCollection(new ArrayList<>(), List.class);
        assertTrue(collection instanceof Collection);

        Collection<?> collection2 = IterableTypes.asCollection(new ArrayList<>(), ArrayList.class);
        assertTrue(collection2 instanceof Collection);
    }
}