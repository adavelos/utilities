package argonath.reflector.types.iterable;

import argonath.reflector.reader.selector.ValueMapper;
import argonath.reflector.reader.selector.ValueMappers;

import java.util.Collection;
import java.util.List;

/**
 * Interface for an Iterable Type. An Iterable Type is a type that can be iterated over while navigating the Object graph.
 * Can be a Collection, List, Set, Map, etc.
 */
public interface IterableType {

    /**
     * Extract iteration information out of an iterable element object.
     */
    IterationElement iterationElement(Integer index, Object object);

    /**
     * Returns an iterable type as a collection. For a collection it is a straight forward cast, but for iterable types
     * it may be different (i.e. for Map it is the entry set).
     */
    default Collection<?> asCollection(Object object) {
        return (Collection<?>) object;
    }

    /**
     * Construct the iterable type instance from a collection. Applicable for non-collection iterable types (like Map)
     */
    Object fromCollection(Collection<?> collection, Class<?> clazz);

    /**
     * Default Value Mapper for the iterable type. This is used to map the value of the element to a different object.
     * This is applicable for non-collection types like Map.
     */
    default ValueMapper valueMapper() {
        return ValueMappers.IDENTITY;
    }

    default Object compose(List<Object> objectList) {
        if (objectList.size() == 1) {
            return objectList.get(0);
        } else {
            throw new IllegalArgumentException("Invalid Iterable Type: " + this.getClass().getName() + ": multiple parameter types");
        }
    }
}
