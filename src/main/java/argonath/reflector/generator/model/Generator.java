package argonath.reflector.generator.model;

import argonath.reflector.generator.GeneratorContext;

public interface Generator<T> {

    T generate(GeneratorContext ctx);

    Class<T> type();

}
