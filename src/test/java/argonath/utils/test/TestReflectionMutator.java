package argonath.utils.test;

import argonath.utils.Assert;
import argonath.utils.reflection.Mutator;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestReflectionMutator {

    @Test
    public void testMutator() {
        TestClass instance1 = Mutator.instantiate(TestClass.class);
        Assert.notNull(instance1, "TestClass instance is not null");

        TestClass2 instance2 = Mutator.instantiate(TestClass2.class);
        Assert.notNull(instance2, "TestClass2 instance is not null");

    }

    private static class TestClass {
        private String name;
        private int age;
        private List<String> friends;
        private byte[] data;

        public TestClass(String name, int age, List<String> friends, byte[] data) {
            this.name = name;
            this.age = age;
            this.friends = friends;
            this.data = data;
        }
    }

    private static class TestClass2 {
        private String name;

    }
}
