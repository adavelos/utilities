package argonath.reflector.types.simple;

/**
 * Interface for a Simple Type.
 * The simple types are types for which Object graph navigation stops and returns the value as per definition of the provided 'toString' method.
 * The simple type also is generated automatically by assigning a string value to the object, hence it must define the 'fromString' method.
 */
public interface SimpleType<T> {
    String toString(T object);

    T fromString(String string);
}
