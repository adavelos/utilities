package argonath.utils.reflector.generator;

public enum GeneratorStrategy {
    ALL,    // populate all non-configured fields
    RANDOM, // populate non-configured fields randomly
    NONE    // populate only configured fields
}
