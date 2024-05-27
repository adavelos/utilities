package argonath.utils.reflector.reader;

import argonath.utils.reflection.ObjectFactoryStrategy;
import argonath.utils.reflector.reader.selector.Selector;

public class Context {
    enum ExtractMode {
        LIST, GET;

        boolean isList() {
            return this == LIST;
        }

        boolean isGet() {
            return this == GET;
        }
    }

    private Selector selector;
    private ObjectFactoryStrategy strategy;
    private Object object;
    private ExtractMode mode;

    public Context(Selector selector, ObjectFactoryStrategy strategy, Object object, ExtractMode mode) {
        this.selector = selector;
        this.strategy = strategy;
        this.object = object;
        this.mode = mode;
    }

    public Selector selector() {
        return selector;
    }

    public ObjectFactoryStrategy strategy() {
        return strategy;
    }

    public Object object() {
        return object;
    }

    public ExtractMode extractMode() {
        return mode;
    }
}

