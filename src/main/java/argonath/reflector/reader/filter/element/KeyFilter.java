package argonath.reflector.reader.filter.element;

import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;
import argonath.utils.Assert;

import java.util.Collection;

public class KeyFilter extends ElementFilter {
    private String key;

    public KeyFilter(String key) {
        Assert.notNull(key, "Key must not be null");
        this.key = key;
    }

    @Override
    public boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext) {
        return super.accept(element.key(), key);
    }

}
