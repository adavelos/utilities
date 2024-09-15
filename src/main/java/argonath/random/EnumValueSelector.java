package argonath.random;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;


/**
 * Extension of the ValueSelector class that is specifically designed to work with Enum values.
 */
public class EnumValueSelector<T extends Enum<T>> extends ValueSelector<T> {

    private List<T> values;
    private List<T> originalValues;

    private boolean withReplacement;


    public EnumValueSelector(boolean withReplacement, Class<T> enumClass) {
        super(withReplacement, getEnumValues(enumClass));
    }

    private static <T extends Enum<T>> Set<T> getEnumValues(Class<T> enumClass) {
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException("The provided class must be an Enum class");
        }
        return EnumSet.allOf(enumClass);
    }

}