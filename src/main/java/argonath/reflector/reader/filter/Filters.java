package argonath.reflector.reader.filter;

import argonath.reflector.factory.ObjectFactory;
import argonath.reflector.reader.ReaderContext;
import argonath.reflector.reader.selector.ValueMapper;
import argonath.reflector.reader.selector.ValueMappers;
import argonath.reflector.types.iterable.IterableType;
import argonath.reflector.types.iterable.IterationElement;

import java.util.Collection;

public class Filters {
    private Filters() {
    }

    public static final Filter NO_FILTER = (element, collection, readerContext) -> true;

    public static Collection<Object> apply(Filter filter, ValueMapper valueMapper, Collection<Object> inputCollection, ReaderContext context, IterableType iterableType) {
        if (filter == Filters.NO_FILTER && valueMapper == ValueMappers.IDENTITY) {
            return inputCollection;
        }

        // In case of no value mapper, use the value mapper of the iterable type (i.e. for MAP to extract VALUE)
        if (valueMapper == ValueMappers.IDENTITY) {
            valueMapper = iterableType.valueMapper();
        }

        // Filter Process
        int index = 0;
        Collection<Object> filteredCollection = ObjectFactory.create(inputCollection.getClass());
        for (Object object : inputCollection) {
            IterationElement element = iterableType.iterationElement(index, object);
            if (filter.accept(element, inputCollection, context)) {
                Object mappedObject = valueMapper.apply(object);
                filteredCollection.add(mappedObject);
            }
            index++;
        }

        return filteredCollection;
    }
}
