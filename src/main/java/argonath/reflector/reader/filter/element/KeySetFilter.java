package argonath.reflector.reader.filter.element;

import argonath.reflector.reader.filter.Filter;
import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;

import java.util.Collection;
import java.util.Set;

public class KeySetFilter implements Filter {
    private Set<String> keySet;

    public KeySetFilter(Set<String> keySet) {
        this.keySet = keySet;
    }

    @Override
    public boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext) {
        return keySet.contains(element.key());
    }
}
