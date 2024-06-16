package argonath.utils.reflector.generator;

import java.util.HashMap;
import java.util.Map;

public class GeneratorConfig<T> {

    private Class<T> clazz;
    private Map<String, String> specs;
    private Map<String, Object> values;

    private GeneratorStrategy strategy;

    // TODO:
    // - generators (builtin, custom)
    // - code lists
    // - debug
    // - strategy (in absence of specs - generate all, randomly generate)

    public GeneratorConfig(Class<T> clazz, GeneratorStrategy strategy) {
        this.clazz = clazz;
        this.specs = new HashMap<>();
        this.values = new HashMap<>();
        this.strategy = strategy;
    }

    public GeneratorConfig withStrategy(GeneratorStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public T generate() {
        T ret = null;
        return ret;
    }

    public GeneratorStrategy strategy() {
        return this.strategy;
    }

    public String specs(String path) {
        return this.specs.get(path);
    }

    public Object value(String path) {
        return this.values.get(path);
    }
}
