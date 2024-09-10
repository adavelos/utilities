package argonath.reflector.generator;

import argonath.reflector.generator.model.Generator;
import argonath.utils.Assert;

public class SequenceGenerator implements Generator<Integer> {

    private int start;
    private int step;
    private String key;

    public SequenceGenerator(int start, int step, String key) {
        this.start = start;
        this.step = step;
        this.key = key;
    }

    @Override
    public Integer generate(Long seed, Object... args) {
        Assert.isTrue(args.length == 1, "SequenceGenerator requires exactly one argument");
        Assert.isTrue(args[0] instanceof Number, "SequenceGenerator requires a number as argument");
        int index = ((Number) args[0]).intValue();
        return start + index * step;
    }

    @Override
    public Class<Integer> type() {
        return Integer.class;
    }

    public String key() {
        return key;
    }
}