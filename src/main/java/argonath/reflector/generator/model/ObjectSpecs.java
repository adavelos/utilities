package argonath.reflector.generator.model;

public class ObjectSpecs<T> {

    private Generator<T> generator;
    private Optionality optionality;
    private Cardinality cardinality;

    private Class<T> typeClass;

    private ObjectSpecs(Class<T> typeClass) {
        this.generator = null;
        this.optionality = Optionality.defaultValue();
        this.cardinality = Cardinality.DEFAULT;
        this.typeClass = typeClass;
    }

    ObjectSpecs(Class<T> typeClass, Generator<T> generator, Optionality optional, Cardinality cardinality) {
        this.generator = generator;
        this.optionality = optional;
        this.cardinality = cardinality;
        this.typeClass = typeClass;
    }

    ObjectSpecs(Generator<T> generator, Optionality optional, Cardinality cardinality) {
        this.generator = generator;
        this.optionality = optional;
        this.cardinality = cardinality;
        this.typeClass = generator != null ? generator.type() : null;
    }

    // Builders

    public static <T> ObjectSpecs<T> create(Class<T> typeClass) {
        return new ObjectSpecs<>(typeClass);
    }

    public static <T> ObjectSpecs<T> generator(Class<T> typeClass, Generator<T> generator) {
        return new ObjectSpecs<>(typeClass, generator, Optionality.defaultValue(), Cardinality.DEFAULT);
    }

    public static <T> ObjectSpecs<T> size(Class<T> typeClass, int minSize, int maxSize) {
        return new ObjectSpecs<>(typeClass, null, Optionality.defaultValue(), new Cardinality(minSize, maxSize));
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

    public static <T> ObjectSpecs<T> merge(ObjectSpecs<T> curSpecs, ObjectSpecs<T> specs) {
        Generator<T> generator = specs.generator != null ? specs.generator : curSpecs.generator;
        Optionality optional = specs.optionality != null ? specs.optionality : curSpecs.optionality;
        Cardinality cardinality = specs.cardinality != null ? specs.cardinality : curSpecs.cardinality;
        return new ObjectSpecs<>(specs.typeClass, generator, optional, cardinality);
    }

}
