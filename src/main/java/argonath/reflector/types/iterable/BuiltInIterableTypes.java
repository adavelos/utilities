package argonath.reflector.types.iterable;

import argonath.reflector.factory.ObjectFactory;
import argonath.reflector.reader.selector.ValueMapper;
import argonath.reflector.reader.selector.ValueMappers;
import argonath.reflector.reflection.Primitives;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.types.builtin.Collections;

import java.lang.reflect.Array;
import java.util.*;

class BuiltInIterableTypes {

    static final IterableType COLLECTION = new CollectionType();
    static final IterableType LIST = new ListType();
    static final IterableType LINKED_LIST = new LinkedListType();
    static final IterableType ARRAY = new ArrayType();
    static final IterableType SET = new SetType();
    static final IterableType MAP = new MapType();


    static class CollectionType implements IterableType {

        @Override
        public IterationElement iterationElement(Integer index, Object object) {
            return new IterationElement(index, object, object);
        }

        @Override
        public Collection<?> asCollection(Object object) {
            return (Collection<?>) object;
        }

        @Override
        public Object fromCollection(Collection<?> collection, Class<?> clazz) {
            return new ArrayList<>(collection);
        }
    }

    static class ListType extends CollectionType {
    }

    static class LinkedListType extends ListType {
        @Override
        public Object fromCollection(Collection<?> collection, Class<?> clazz) {
            return new LinkedList<>(collection);
        }
    }

    static class SetType implements IterableType {
        @Override
        public IterationElement iterationElement(Integer index, Object object) {
            return new IterationElement(null, object, object);
        }

        @Override
        public Collection<?> asCollection(Object object) {
            return (Set<?>) object;
        }

        @Override
        public Object fromCollection(Collection<?> collection, Class<?> clazz) {
            return new HashSet<>(collection);
        }
    }


    /**
     * For Maps the behavior of the Object Reader is the following:
     * - When accessing the map without filter expression, the Map instance is returned
     * - When a filter expression is applied and the result is multiple values, the Map is transformed to a Set of value instances
     * matching the filter expression (i.e. for Map<String, String> it returns a HashSet<String>)
     * - When a filter expression is applied and the result is a single value, then the value is returned directly
     */
    static class MapType implements IterableType {
        @Override
        public IterationElement iterationElement(Integer index, Object object) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
            return new IterationElement(null, entry.getKey(), entry.getValue());
        }

        @Override
        public Collection<?> asCollection(Object object) {
            Map<?, ?> asMap = (Map<?, ?>) object;
            return new ArrayList<>(asMap.entrySet());
        }

        @Override
        public Object fromCollection(Collection<?> collection, Class<?> clazz) {
            Class<?> elementType = ReflectiveAccessor.getGenericType(collection);
            if (!Map.Entry.class.isAssignableFrom(elementType)) {
                // Value Mapper has been applied, therefore Map cannot be reconstructed. Return list of extracted values
                // instead (key or value, depending on the value mapper applied)
                return collection;
            }

            // There is no value mapper applied, so directly transform to the original Map
            Map map = (Map) ObjectFactory.create(clazz);
            collection.forEach(entry -> {
                Map.Entry mapEntry = (Map.Entry) entry;
                map.put(mapEntry.getKey(), mapEntry.getValue());
            });
            return map;
        }

        @Override
        public ValueMapper valueMapper() {
            return ValueMappers.mapValueMapper();
        }

        @Override
        public Object compose(List<Object> objectList) {
            if (objectList.size() == 2) {
                return new AbstractMap.SimpleEntry<>(objectList.get(0), objectList.get(1));
            } else {
                throw new IllegalArgumentException("Invalid Iterable Type: " + this.getClass().getName() + ": invalid number of parameter types");
            }
        }
    }

    static class ArrayType implements IterableType {
        @Override
        public IterationElement iterationElement(Integer index, Object object) {
            return new IterationElement(index, object, object);
        }

        @Override
        public Collection<?> asCollection(Object object) {
            if (ReflectiveAccessor.isArrayOfPrimitives(object.getClass())) {
                return Collections.fromArrayOfPrimitives(object);
            }
            return new ArrayList<>(Arrays.asList((Object[]) object));
        }

        @Override
        public Object fromCollection(Collection<?> collection, Class<?> clazz) {
            Class<?> elementType = ReflectiveAccessor.getArrayType(clazz);
            if (elementType.isPrimitive()) {
                // expected field type is primitive, so convert to primitive array
                elementType = Primitives.getWrapperType(elementType);
            }

            // Create an array of the correct type and size
            Object array = Array.newInstance(elementType, collection.size());

            // Convert collection to array of elementType
            int index = 0;
            for (Object item : collection) {
                Class<?> componentType = elementType.getComponentType();
                Object arrayItem;
                if (componentType != null && componentType.isPrimitive() && item.getClass().isArray()) {
                    // array of primitives
                    arrayItem = Primitives.convertToPrimitiveArray(item, componentType);
                } else {
                    arrayItem = elementType.cast(item);
                }
                Array.set(array, index++, arrayItem);
            }

            return array;
        }
    }
}
