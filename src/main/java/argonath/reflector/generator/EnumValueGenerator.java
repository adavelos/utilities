package argonath.reflector.generator;

import argonath.random.EnumValueSelector;
import argonath.reflector.generator.model.Generator;

/**
 * Wrapper of the 'EnumValueSelector' class that implements the 'Generator' interface.
 */
public class EnumValueGenerator<T extends Enum<T>> implements Generator<T> {

    private EnumValueSelector<T> valueSelector;

    private Class<T> clazz;

    public EnumValueGenerator(Class<T> enumClass, boolean withReplacement) {
        this.valueSelector = new EnumValueSelector<>(withReplacement, enumClass);
        this.clazz = enumClass;
    }

    @Override
    public T generate(Long seed, Object... args) {
        return valueSelector.drawValue(seed, args);
    }

    @Override
    public Class<T> type() {
        return clazz;
    }

}