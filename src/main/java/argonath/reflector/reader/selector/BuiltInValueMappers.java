package argonath.reflector.reader.selector;

import java.util.Map;

class BuiltInValueMappers {
    private BuiltInValueMappers() {
    }

    static final ValueMapper MAP_KEY_VM = new ValueMapper() {

        @Override
        public Object apply(Object input) {
            return ((Map.Entry<?, ?>) input).getKey();
        }
    };

    static final ValueMapper MAP_VALUE_VM = new ValueMapper() {

        @Override
        public Object apply(Object input) {
            return ((Map.Entry<?, ?>) input).getValue();
        }
    };
}
