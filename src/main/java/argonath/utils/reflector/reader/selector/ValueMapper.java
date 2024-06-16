package argonath.utils.reflector.reader.selector;

import argonath.utils.reflector.reader.expression.Method;

public class ValueMapper {
    public static Object apply(Object input, Method valueMapper) {
        if (valueMapper.isIdentity()) {
            return input;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }


}
