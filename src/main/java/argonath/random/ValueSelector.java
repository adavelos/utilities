package argonath.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * A generic value selector that implements the Generator interface.
 * The generator draws values from a given set of values. Depending on the 'withReplacement' flag, the generator may or may not replace the drawn values.
 * <p>
 * When the set of values is exhausted, the generator will reset and start drawing values from the original set of values.
 */
public class ValueSelector<T> {

    private List<T> values;
    private List<T> originalValues;

    private boolean withReplacement;


    public ValueSelector(boolean withReplacement, Set<T> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Value Selector: Values set cannot be empty");
        }
        this.originalValues = List.copyOf(values);
        this.withReplacement = withReplacement;
        reset();
    }

    public T drawValue(Long seed) {
        int size = values.size();
        if (size == 0) {
            reset();
            size = values.size();
        }
        int index = RandomNumber.getInteger(0, size);
        T value = values.get(index);
        if (!withReplacement) {
            values.remove(index);
        }
        return value;
    }

    private void reset() {
        values = new ArrayList<>(originalValues);
    }

}