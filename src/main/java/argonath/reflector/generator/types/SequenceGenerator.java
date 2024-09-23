package argonath.reflector.generator.types;

import argonath.reflector.generator.GeneratorContext;
import argonath.reflector.generator.model.Generator;

public class SequenceGenerator implements Generator<Integer> {

    private int step;

    private int current;

    public SequenceGenerator(int start, int step) {
        this.step = step;
        this.current = start;
    }

    @Override
    public Integer generate(GeneratorContext ctx) {
        int result = current;
        current += step;
        if (current < 0) { // overflow
            current = 0;
        }
        return result;
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }

}