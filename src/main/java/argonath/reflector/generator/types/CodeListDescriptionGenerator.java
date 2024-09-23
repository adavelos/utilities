package argonath.reflector.generator.types;

import argonath.reflector.generator.GeneratorContext;
import argonath.reflector.generator.codelist.CodeListItem;
import argonath.reflector.generator.model.Generator;

/**
 * Wrapper of the 'ValueSelector' class that implements the 'Generator' interface.
 */
public class CodeListDescriptionGenerator implements Generator<String> {

    private String key;

    public CodeListDescriptionGenerator(String key) {
        this.key = key;
    }

    @Override
    public String generate(GeneratorContext ctx) {
        CodeListItem<?> curItem = ctx.getCurrentCodeListItem(key);
        if (curItem == null) {
            throw new IllegalArgumentException("Current code list item not found in Generator Context: " + key);
        }
        return curItem.getDescription();
    }

    @Override
    public Class<String> type() {
        return String.class;
    }

}