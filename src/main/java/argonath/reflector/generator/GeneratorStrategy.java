package argonath.reflector.generator;

import argonath.random.RandomNumber;
import argonath.reflector.generator.model.Optionality;

public enum GeneratorStrategy {
    ALL {
        public boolean populate(Optionality optionality) {
            return true;
        }
    },
    RANDOM {
        public boolean populate(Optionality optionality) {
            switch (optionality) {
                case MANDATORY:
                    return true;
                case OPTIONAL:
                    return RandomNumber.flipCoin();
                case NEVER:
                    return false;
            }
            throw new IllegalStateException("Unknown optionality: " + optionality);
        }
    },
    NONE {
        public boolean populate(Optionality optionality) {
            return false;
        }
    };


    abstract boolean populate(Optionality optionality);

    //default strategy = RANDOM
    public static GeneratorStrategy defaultStrategy() {
        return RANDOM;
    }

}
