package argonath.reflector.reader;

import argonath.reflector.reader.selector.ValueSelector;

public class ReaderContext {


    enum ResolutionMode {
        LIST, GET;

        boolean isList() {
            return this == LIST;
        }

        boolean isGet() {
            return this == GET;
        }
    }

    private ValueSelector selector;
    private Object object;
    private ResolutionMode mode;

    private Class<?> requestedClass;

    private boolean castResult = false;

    public ReaderContext(ValueSelector selector, Object object, ResolutionMode mode, Class<?> requestedClass) {
        this.selector = selector;
        this.object = object;
        this.mode = mode;
        if (requestedClass == null || requestedClass == Object.class) {
            this.castResult = false;
            this.requestedClass = Object.class;
        } else {
            this.castResult = true;
            this.requestedClass = requestedClass;
        }
    }

    public ValueSelector selector() {
        return selector;
    }

    public Object object() {
        return object;
    }

    public ResolutionMode resolutionMode() {
        return mode;
    }

    public Class<?> requestedClass() {
        return requestedClass;
    }

    public boolean castResult() {
        return castResult;
    }
}

