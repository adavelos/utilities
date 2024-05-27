package argonath.utils.reflector.reader.mapper;

import argonath.utils.reflector.reader.Context;

public interface ValueMapper {
    Object apply(Object extractedObject, Context object);
}
