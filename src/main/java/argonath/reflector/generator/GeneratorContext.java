package argonath.reflector.generator;

import argonath.reflector.generator.model.Cardinality;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.generator.model.Optionality;
import argonath.reflector.reflection.TypeExplorer;
import argonath.utils.XPathUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

public class GeneratorContext {

    private Class<?> returnClass;

    private GeneratorConfig config;

    private Stack<Element> elements;

    private Element curElement;

    private Long seed = null;

    private Map<String, Sequence> sequenceMap;

    GeneratorContext(Class<?> returnClass, GeneratorConfig config) {
        this.returnClass = returnClass;
        this.config = config;
        this.elements = new Stack<>();
        this.sequenceMap = new HashMap<>();
    }

    GeneratorConfig config() {
        return this.config;
    }

    GeneratorStrategy strategy() {
        return this.config.strategy();
    }

    Element rootElement() {
        return Element.root(this);
    }

    String path() {
        List<String> pathElements = elements.stream()
                .map(Element::pathElementName)
                .collect(Collectors.toList());
        return XPathUtil.create(pathElements);
    }

    ObjectSpecs<?> specs() {
        return this.config.specs(curElement, this);
    }

    Generator<?> generator() {
        ObjectSpecs<?> specs = this.config.specs(curElement, this);
        Generator<?> generator = (specs == null) ? null : specs.generator();
        if (generator == null) {
            generator = GeneratorRegistry.generator(curElement.typeClass(), path());
        }
        return generator;
    }

    Cardinality cardinality() {
        ObjectSpecs<?> specs = this.config.specs(curElement, this);
        return specs == null ? Cardinality.DEFAULT : specs.cardinality();
    }

    // optionality
    Optionality optionality() {
        ObjectSpecs<?> specs = this.config.specs(curElement, this);
        return specs == null ? Optionality.defaultValue() : specs.optionality();
    }

    Object value() {
        return this.config.value(curElement, this);
    }

    boolean hasSpecs() {
        return specs() != null;
    }

    boolean hasValue() {
        return value() != null;
    }

    boolean isRoot() {
        return curElement.isRoot();
    }

    // wrapper for dopopulate

    boolean doPopulate(Element element) {
        if (isRoot()) {
            return true;
        }
        if (element.type != null && element.type.getClass().isPrimitive()) {
            return true; // always populate primitives
        }
        return this.strategy().populate(optionality());
    }

    void enter(Element element) {
        elements.push(element);
        curElement = element;
    }

    void exit() {
        elements.pop();
    }

    void withSeed(Long seed) {
        this.seed = seed;
    }

    Long seed() {
        return this.seed;
    }

    public Sequence sequence(String path) {
        return sequenceMap.compute(path, (key, existingSeq) -> {
            if (existingSeq == null) {
                return new Sequence();
            }
            return existingSeq;
        });
    }

    static class Element {

        Field field;
        Type type;
        int index;
        Object parent;
        GeneratorContext ctx;

        private Element(Field field, Type type, int index, Object parent, GeneratorContext ctx) {
            this.field = field;
            this.index = index;
            this.type = type;
            this.parent = parent;
            this.ctx = ctx;
        }

        static Element ofField(Field field, Object parent, GeneratorContext ctx) {
            return new Element(field, field.getGenericType(), 0, parent, ctx);
        }

        static Element ofParam(Type type, int index, GeneratorContext ctx) {
            return new Element(null, type, index, null, ctx);
        }

        static Element root(GeneratorContext ctx) {
            return new Element(null, null, 0, null, ctx);
        }

        Field field() {
            return field;
        }

        Type type() {
            return type;
        }

        Class<?> typeClass() {
            if (field != null) {
                return field.getType();
            }
            if (type != null) {
                return TypeExplorer.typeClass(type);
            }
            return ctx.returnClass;
        }

        int index() {
            return index;
        }

        boolean isRoot() {
            return field == null;
        }

        String name() {
            return field != null ? field.getName() : ctx.returnClass.getName();
        }

        String pathElementName() {
            return field != null ? field.getName() : "";
        }

        GeneratorContext ctx() {
            return ctx;
        }

        public Object parent() {
            return parent;
        }
    }

}

