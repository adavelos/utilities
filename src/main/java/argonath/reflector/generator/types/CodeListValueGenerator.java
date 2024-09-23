package argonath.reflector.generator.types;

import argonath.random.ValueSelector;
import argonath.reflector.generator.GeneratorContext;
import argonath.reflector.generator.codelist.CodeList;
import argonath.reflector.generator.codelist.CodeListItem;
import argonath.reflector.generator.model.Generator;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Wrapper of the 'ValueSelector' class that implements the 'Generator' interface.
 */
public class CodeListValueGenerator<T> implements Generator<T> {

    private String key;
    private boolean withReplacement;
    private ValueSelector<CodeListItem<T>> valueSelector;

    private Class<T> clazz;

    public CodeListValueGenerator(Class<T> clazz, String key, boolean withReplacement) {
        this.key = key;
        this.withReplacement = withReplacement;
        this.clazz = clazz;
    }

    @Override
    public T generate(GeneratorContext ctx) {
        if (valueSelector == null) {
            @SuppressWarnings("unchecked")
            CodeList<T> cl = ctx.getCodeList(key);
            if (cl == null) {
                throw new IllegalArgumentException("Code list not found in Generator Context: " + key);
            }
            List<CodeListItem<T>> items = cl.items();
            valueSelector = new ValueSelector<>(withReplacement, new LinkedHashSet<>(items));
        }

        CodeListItem<T> codeListItem = valueSelector.drawValue(ctx.seed());
        if (codeListItem == null) {
            throw new IllegalArgumentException("Failed to draw value from code list: " + key);
        }
        T code = codeListItem.getCode();
        ctx.setCurrentCodeListItem(key, codeListItem);
        return code;
    }

    @Override
    public Class<T> type() {
        return clazz;
    }

}