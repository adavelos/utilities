package argonath.utils.reflector.reader.types;

import argonath.utils.reflection.ObjectFactoryStrategy;
import argonath.utils.reflection.ReflectiveFactory;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Useful methods for dealing with collections.
 */
@SuppressWarnings("unchecked")
public class Collections {

    /**
     * In case the given argument is a collection of collections, this method will flatten it to a single collection.
     */
    public static <T> List<T> flatten(List<T> rawCollection) {
        if (rawCollection == null) {
            return null;
        }
        if (rawCollection.isEmpty()) {
            return rawCollection;
        }
        if (!(rawCollection.iterator().next() instanceof Collection)) {
            return rawCollection;
        }

        List<Collection<T>> collections = (List<Collection<T>>) rawCollection;
        return collections.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static <T> List<T> emptyList(ObjectFactoryStrategy strategy) {
        return ReflectiveFactory.instantiateList(strategy.defaultListClass());
    }

    public static <T> T emptyCollection(Class<T> collectionClass, ObjectFactoryStrategy strategy) {
        T ret = ReflectiveFactory.instantiateCollection(collectionClass, strategy.defaultListClass());
        return ret;
    }

    public static <T> T emptyCollection(T object, ObjectFactoryStrategy strategy) {
        Class<T> clazz = (Class<T>) object.getClass();
        return emptyCollection(clazz, strategy);
    }

}
