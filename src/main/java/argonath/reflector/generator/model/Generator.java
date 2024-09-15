package argonath.reflector.generator.model;

public interface Generator<T> {

    T generate(Long seed);

    Class<T> type();

}
