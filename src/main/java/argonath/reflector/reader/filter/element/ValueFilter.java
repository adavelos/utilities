package argonath.reflector.reader.filter.element;

import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;
import argonath.utils.Assert;

import java.util.Collection;

public class ValueFilter extends ElementFilter {
    private String value;

    public ValueFilter(String value) {
        Assert.notNull(value, "Value must not be null");
        this.value = value;
    }

    @Override
    public boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext) {
        return super.accept(element.value(), value);
    }


}
