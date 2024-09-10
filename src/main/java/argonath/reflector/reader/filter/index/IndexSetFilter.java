package argonath.reflector.reader.filter.index;

import argonath.reflector.reader.filter.Filter;
import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;

import java.util.Collection;
import java.util.Set;

public class IndexSetFilter implements Filter {
    private Set<Integer> indexSet;

    public IndexSetFilter(Set<Integer> indexSet) {
        this.indexSet = indexSet;
    }

    @Override
    public boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext) {
        return indexSet.contains(element.index());
    }
}
