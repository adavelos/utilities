package argonath.utils.reflector.reader.types;

/**
 * Interface for a Final Type.
 * The simple types are types for which Object graph navigation stops and returns the value as per definition of the provided
 * 'toString' method.
 */
public interface FinalType<T> {
    String toString(T object);
}
