package argonath.reflector.reader.filter;

import argonath.reflector.reader.ReaderContext;
import argonath.reflector.types.iterable.IterationElement;

import java.util.Collection;

public interface Filter {

    boolean accept(IterationElement element, Collection<?> collection, ReaderContext readerContext);

}
