package argonath.reflector.reader.filter.element;

import argonath.reflector.reader.filter.Filter;
import argonath.utils.WcMatcher;

import java.util.Objects;

public abstract class ElementFilter implements Filter {

    protected boolean accept(Object actualValueObj, String value) {
        if (actualValueObj == null) {
            return false;
        }
        String actualValue = Objects.toString(actualValueObj);
        if (value.contains("*")) {
            return WcMatcher.match(actualValue, value);
        }
        return value.equals(actualValue);
    }


}
