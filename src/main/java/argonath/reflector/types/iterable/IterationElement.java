package argonath.reflector.types.iterable;

public class IterationElement {

    private Integer index;
    private Object key;
    private Object value;

    public IterationElement(Integer index, Object key, Object value) {
        this.index = index;
        this.key = key;
        this.value = value;
    }

    public Integer index() {
        return index;
    }

    public Object key() {
        return key;
    }

    public Object value() {
        return value;
    }

}
