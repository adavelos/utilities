package argonath.utils.test.generator.model;

public class GenericClass<T extends Comparable<T>> {
    public T field;

    public T getField() {
        return field;
    }
}

