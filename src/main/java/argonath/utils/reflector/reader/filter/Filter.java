package argonath.utils.reflector.reader.filter;

import argonath.utils.reflector.reader.Context;

import java.util.Collection;

public interface Filter {
    Collection<Object> apply(Collection<Object> elements, Context context);
}
