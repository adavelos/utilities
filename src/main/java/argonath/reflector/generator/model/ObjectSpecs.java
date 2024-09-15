package argonath.reflector.generator.model;

public class ObjectSpecs<T> {

    private Generator<T> generator;
    private Optionality optionality;
    private Cardinality cardinality;

    private ObjectSpecs() {
        this.generator = null;
        this.optionality = Optionality.defaultValue();
        this.cardinality = Cardinality.DEFAULT;
    }

    ObjectSpecs(Generator<?> generator, Optionality optional, Cardinality cardinality) {
        this.generator = (Generator<T>) generator;
        this.optionality = optional;
        this.cardinality = cardinality;
    }

    // Builders

    public static <T> ObjectSpecs<T> create(Class<T> typeClass) {
        return new ObjectSpecs<>();
    }

    public static <T> ObjectSpecs<T> generator(Class<T> typeClass, Generator<T> generator) {
        return create(typeClass).generator(generator);
    }

    public static <T> ObjectSpecs<T> size(Class<T> typeClass, int minSize, int maxSize) {
        return create(typeClass).size(minSize, maxSize);
    }

    public static <T> ObjectSpecs<T> distribution(Class<T> typeClass, Generator<Integer> distribution) {
        return create(typeClass).distribution(distribution);
    }

    public static <T> ObjectSpecs<T> mandatory(Class<T> typeClass) {
        return create(typeClass).mandatory();
    }

    public static <T> ObjectSpecs<T> optional(Class<T> typeClass) {
        return create(typeClass).optional();
    }

    // Chainable methods

    public ObjectSpecs<T> generator(Generator<T> generator) {
        this.generator = generator;
        return this;
    }

    public ObjectSpecs<T> size(int minSize, int maxSize) {
        this.cardinality = new Cardinality(minSize, maxSize);
        return this;
    }

    public ObjectSpecs<T> size(int exactSize) {
        this.cardinality = new Cardinality(exactSize, exactSize);
        return this;
    }

    public ObjectSpecs<T> mandatory() {
        this.optionality = Optionality.MANDATORY;
        return this;
    }

    public ObjectSpecs<T> optional() {
        this.optionality = Optionality.OPTIONAL;
        return this;
    }

    public ObjectSpecs<T> ignore() {
        this.optionality = Optionality.NEVER;
        return this;
    }

    public ObjectSpecs<T> distribution(Generator<Integer> distribution) {
        this.cardinality = new Cardinality(distribution);
        return this;
    }

    public Generator<T> generator() {
        return generator;
    }

    public Optionality optionality() {
        return optionality;
    }

    public Cardinality cardinality() {
        return cardinality;
    }

    public static ObjectSpecs<?> merge(ObjectSpecs<?> curSpecs, ObjectSpecs<?> specs) {
        Generator<?> generator = specs.generator != null ? specs.generator : curSpecs.generator;
        Optionality optional = specs.optionality != null ? specs.optionality : curSpecs.optionality;
        Cardinality cardinality = specs.cardinality != null ? specs.cardinality : curSpecs.cardinality;
        return new ObjectSpecs<>(generator, optional, cardinality);
    }

}
