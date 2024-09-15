package argonath.reflector.generator.model;

/**
 * Functional interface to define generator template for building generators from Specs EL expressions.
 */
public interface GeneratorTemplate {

    Generator<?> buildGenerator(String[] args);
}
