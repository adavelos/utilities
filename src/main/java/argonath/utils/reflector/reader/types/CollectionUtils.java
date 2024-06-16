package argonath.utils.reflector.reader.types;

import argonath.utils.reflection.ObjectFactoryStrategy;
import argonath.utils.reflection.ReflectiveFactory;
import argonath.utils.reflector.reader.ObjectReader;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Useful methods for dealing with collections.
 */
@SuppressWarnings("unchecked")
public class CollectionUtils {

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
