package argonath.reflector.reader.filter.index;

import argonath.reflector.reader.filter.Filter;
import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;
import argonath.utils.Assert;
import org.apache.commons.lang3.Range;

import java.util.Collection;

public class IndexRangeFilter implements Filter {
    private Integer min;
    private Integer max;

    public IndexRangeFilter(Integer min, Integer max) {
        Assert.notNull(min, "Min must not be null");
        this.min = min;
        this.max = max;
    }

    public IndexRangeFilter(Integer min) {
        this(min, null); // there is no upper bound
    }

    @Override
    public boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext) {
        Integer index = element.index();
        if (max == null) {
            return index >= min;
        }
        return Range.of(min, max).contains(index);
    }
}
