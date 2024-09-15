package argonath.reflector.generator;

import argonath.random.ValueSelector;
import argonath.reflector.generator.model.Generator;

import java.util.Set;

/**
 * Wrapper of the 'ValueSelector' class that implements the 'Generator' interface.
 */
public class ValueGenerator<T> implements Generator<T> {

    private ValueSelector<T> valueSelector;

    private Class<T> clazz;

    public ValueGenerator(Class<T> clazz, boolean withReplacement, Set<T> values) {
        this.valueSelector = new ValueSelector<>(withReplacement, values);
        this.clazz = clazz;
    }

    public ValueGenerator(Class<T> clazz, boolean withReplacement, T... values) {
        this(clazz, withReplacement, Set.of(values));
    }

    @Override
    public T generate(Long seed) {
        return valueSelector.drawValue(seed);
    }

    @Override
    public Class<T> type() {
        return clazz;
    }

}