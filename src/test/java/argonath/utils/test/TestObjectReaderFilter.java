package argonath.utils.test;

import argonath.utils.reflector.reader.ObjectReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestObjectReaderFilter {

    @Test
    public void testSingleFilter() {
        ObjectReader reader = ObjectReader.create();
        InnerClass innerClass = new InnerClass("1,", List.of("innerFriend1", "innerFriend2", "innerFriend3"));
        List<InnerClass> innerClasses = List.of(
                new InnerClass("2", List.of("innerFriend4", "innerFriend5")),
                new InnerClass("3", List.of("innerFriend6", "innerFriend7")),
                new InnerClass("4", List.of("innerFriend8", "innerFriend9"))
        );
        TestClass inputObject = new TestClass("1", List.of("friend1", "friend2"), innerClass, innerClasses);

        // Assert that friends[1] is equal to the second element in the friends list
        String friend1 = reader.get(inputObject, "/friends[1]", String.class);
        Assertions.assertEquals(inputObject.friends.get(1), friend1, "friends[1] is equal to the second element in the friends list");
        // Assert that innerClass.friends[1] is equal to the second element in the innerClass.friends list
        String innerFriend1 = reader.get(inputObject, "/innerClass/friends[1]", String.class);
        Assertions.assertEquals(innerClass.friends.get(1), innerFriend1, "innerClass.friends[1] is equal to the second element in the innerClass.friends list");


    }

    public static class TestClass {
        private String id;
        private List<String> friends;
        private InnerClass innerClass;
        private List<InnerClass> innerClasses;

        public TestClass(String id, List<String> friends, InnerClass innerClass,
                         List<InnerClass> innerClasses) {
            this.id = id;
            this.friends = friends;
            this.innerClass = innerClass;
            this.innerClasses = innerClasses;
        }
    }

    public static class InnerClass {
        private String id;
        private List<String> friends;

        public InnerClass(String id, List<String> friends) {
            this.id = id;
            this.friends = friends;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            InnerClass innerClass = (InnerClass) obj;
            return this.id.equals(innerClass.id);
        }

    }
}
