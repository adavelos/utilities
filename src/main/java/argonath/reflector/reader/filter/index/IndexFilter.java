package argonath.reflector.reader.filter.index;

import argonath.reflector.reader.filter.Filter;
import argonath.reflector.reader.filter.element.ValueFilter;
import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;

import java.util.Collection;

public class IndexFilter implements Filter {
    private int index;

    public IndexFilter(int index) {
        this.index = index;
    }

    @Override
    public boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext) {
        if (element.index() != null) {
            return element.index() == index;
        } else {
            // fall-back in case of non-list iterable type to switch to a value filter
            return new ValueFilter(String.valueOf(index)).accept(element, collection, readerContext);
        }
    }

}
