package argonath.utils.test.generator.model;

public class TestClassWithInterface implements TestInterface {
    String test;

    @Override
    public String test() {
        return test;
    }
}