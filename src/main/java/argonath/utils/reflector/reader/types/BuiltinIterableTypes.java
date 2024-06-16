package argonath.utils.reflector.reader.types;

import argonath.utils.reflection.ObjectFactoryStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines the build-in navigable type definitions
 */
class BuiltinIterableTypes {

    static IterableType LIST = new IterableType() {
        @Override
        public Collection<?> simpleFilter(String value, Collection<?> object) {
            List<?> list = (List<?>) object;
            try {
                int index = Integer.parseInt(value);
                return List.of(list.get(index));
            } catch (NumberFormatException e) {
                return list.stream()
                        .filter(o -> o != null && o.toString().equals(value))
                        .toList();
            }
        }
    };
    static IterableType SET = new IterableType() {
        @Override
        public Collection<?> simpleFilter(String value, Collection<?> object) {
            throw new UnsupportedOperationException("Filtering is not supported for Set");
        }
    };
    static IterableType MAP = new IterableType() {
        @Override
        public Collection<?> asCollection(Object object, ObjectFactoryStrategy strategy) {
            Map<?, ?> asMap = (Map<?, ?>) object;
            Set<?> entrySet = asMap.entrySet();
            return entrySet;
        }

        @Override
        public Collection<?> simpleFilter(String value, Collection<?> object) {
            throw new UnsupportedOperationException("Filtering is not supported for Map");
        }
    };

}
