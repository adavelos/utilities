package argonath.utils.reflector.reader.types;

import argonath.utils.reflection.ObjectFactoryStrategy;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Defines the build-in navigable type definitions
 */
class BuiltinIterableTypes {

    static IterableType LIST = new IterableType() {
    };
    static IterableType SET = new IterableType() {
    };
    static IterableType MAP = new IterableType() {
        @Override
        public Collection<?> asCollection(Object object, ObjectFactoryStrategy strategy) {
            Map<?, ?> asMap = (Map<?, ?>) object;
            Set<?> entrySet = asMap.entrySet();
            return entrySet;
        }
    };

}
