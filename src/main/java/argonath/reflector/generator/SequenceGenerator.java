package argonath.reflector.generator;

import argonath.reflector.generator.model.Generator;

public class SequenceGenerator implements Generator<Integer> {

    private int step;

    private int current;

    public SequenceGenerator(int start, int step) {
        this.step = step;
        this.current = start;
    }

    @Override
    public Integer generate(Long seed, Object... args) {
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