package argonath.reflector.generator.model;

public interface Generator<T> {

    T generate(Long seed, Object... args);

    Class<T> type();

}
