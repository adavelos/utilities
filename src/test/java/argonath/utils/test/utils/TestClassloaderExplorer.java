package argonath.utils.test.utils;

import argonath.reflector.reflection.ClassloaderExplorer;
import argonath.utils.test.utils.model.I1;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestClassloaderExplorer {

    @Test
    public void test() {
        Class<?> res = ClassloaderExplorer.findClosestImplementation(I1.class);
        Assertions.assertEquals("argonath.utils.test.utils.model.sub.C1", res.getName());
    }
}
