package argonath.reflector.generator.model;

import argonath.random.RandomNumber;

public class Cardinality {
    // always have a default cardinality
    public static Cardinality DEFAULT = new Cardinality(1, 3, null);

    private Integer minSize;
    private Integer maxSize;

    private Generator<Integer> distribution;

    public Cardinality(Integer minSize, Integer maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    public Cardinality(Integer minSize, Integer maxSize, Generator<Integer> distribution) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.distribution = distribution;
    }

    public Cardinality(Generator<Integer> distribution) {
        this.distribution = distribution;
        this.minSize = null;
        this.maxSize = null;
    }

    public Generator<Integer> distribution() {
        return distribution;
    }

    public int minSize() {
        return minSize;
    }

    public int maxSize() {
        return maxSize;
    }

    public Integer randomSize() {
        if (distribution != null) {
            return distribution.generate(null);
        }
        return RandomNumber.getInteger(minSize, maxSize + 1);
    }

}
