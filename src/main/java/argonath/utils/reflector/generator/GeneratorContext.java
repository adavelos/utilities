package argonath.utils.reflector.generator;

import java.util.Stack;

public class GeneratorContext {
    private GeneratorConfig config;

    private Stack<ObjectGenerator.Element> elements;

    public GeneratorContext(GeneratorConfig config) {
        this.config = config;
        this.elements = new Stack<>();
    }

    public GeneratorConfig config() {
        return this.config;
    }

    public GeneratorStrategy strategy() {
        return this.config.strategy();
    }

    public String path() {
        return null;
    }

    public String specs() {
        return this.config.specs(path());
    }

    public Object value() {
        return this.config.value(path());
    }

    public boolean hasSpecs() {
        return specs() != null;
    }

    public boolean hasValue() {
        return value() !=null;
    }
}
