package argonath.utils.reflector.reader.selector;

import argonath.utils.reflector.reader.Context;
import argonath.utils.reflector.reader.expression.Expression;
import argonath.utils.reflector.reader.types.IterableType;
import argonath.utils.reflector.reader.types.IterableTypes;

import java.util.Collection;

public class Filter {

    public static Collection<?> applyFilter(Expression expression, Collection<Object> inputCollection, Context context) {
        // if no filter is found, then return the input collection
        if (expression == null) {
            return inputCollection;
        }

        // Resolve Iterable Type
        IterableType iterableType = IterableTypes.iterableType(inputCollection.getClass());

        // Simple Filter
        if (expression.isSimple()) {
            return iterableType.simpleFilter(expression.value(), inputCollection);
        }

        // Expression Filter
        throw new UnsupportedOperationException("Not implemented yet");
    }

}

