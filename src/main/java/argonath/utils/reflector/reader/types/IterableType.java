package argonath.utils.reflector.reader.types;

import argonath.utils.reflection.ObjectFactoryStrategy;

import java.util.Collection;

/**
 * Interface for an Iterable Type. An Iterable Type is a type that can be iterated over while navigating the Object graph.
 * Can be a Collection, List, Set, Map, etc.
 */
interface IterableType {

    /**
     * Returns an iterable type as a collection. For a collection it is a straight forward cast, but for iterable types
     * it may be different (i.e. for Map it is the entry set).
     */
    default Collection<?> asCollection(Object object, ObjectFactoryStrategy strategy) {
        return (Collection<?>) object;
    }
}
