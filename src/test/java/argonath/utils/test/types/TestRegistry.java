package argonath.utils.test.types;

import argonath.reflector.registry.TypeRegistry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestRegistry {

    @Test
    public void testRegistry() {

        // Upwards-only: check that closest match is found upwards
        TypeRegistry<String> registry = new TypeRegistry<>(true);
        registry.register(T1.class, "T1");
        registry.register(I1.class, "I1");
        Assertions.assertTrue("I1".equals(registry.match(T3.class)));
        Assertions.assertTrue("T1".equals(registry.match(T2.class)));

        // Upwards-only: check that no match is found downwards
        TypeRegistry<String> registry2 = new TypeRegistry<>(true);
        registry2.register(T3.class, "T3");
        Assertions.assertFalse("T3".equals(registry2.match(T1.class)));

        // Both directions: check that closest match is found downwards
        TypeRegistry<String> registry3 = new TypeRegistry<>(false);
        registry3.register(T3.class, "T3");
        Assertions.assertTrue("T3".equals(registry3.match(T1.class)));

        // Both directions: check that closest match is found downwards when two classes match
        TypeRegistry<String> registry4 = new TypeRegistry<>(false);
        registry4.register(T3.class, "T3");
        registry4.register(T2.class, "T2");
        Assertions.assertTrue("T2".equals(registry4.match(T1.class)));

    }

    /**
     * T1 -> T2 -> T3
     * I1 -> T3
     * <p>
     * If we register T2 => functionality and T3 => functionality, then registry.match(I1) should return T3 and registry.match(T1) should return T2
     */

    public static class T1 {
        String field;
    }

    public static class T2 extends T1 {
        String field;
    }

    public static class T3 extends T2 implements I1 {
        String field;

        @Override
        public String field() {
            return null;
        }
    }

    public static interface I1 {
        String field();
    }
}


