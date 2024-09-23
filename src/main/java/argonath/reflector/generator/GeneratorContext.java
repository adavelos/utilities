package argonath.reflector.generator;

import argonath.reflector.generator.codelist.CodeList;
import argonath.reflector.generator.codelist.CodeListItem;
import argonath.reflector.generator.model.Cardinality;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.generator.model.Optionality;
import argonath.reflector.reflection.TypeExplorer;
import argonath.utils.XPathUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GeneratorContext {

    private Class<?> returnClass;

    private GeneratorConfig config;

    private Stack<Element> elements;

    private Element curElement;

    private Long seed = null;

    private Map<Pair<String, String>, CodeListItem<?>> currentCodeListItems;

    GeneratorContext(Class<?> returnClass, GeneratorConfig config) {
        this.returnClass = returnClass;
        this.config = config;
        this.elements = new Stack<>();
        this.currentCodeListItems = new HashMap<>();
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
                .toList();
        return XPathUtil.create(pathElements);
    }

    @SuppressWarnings("java:S1452")
    ObjectSpecs<?> specs() {
        return this.config.specs(curElement, this);
    }

    @SuppressWarnings("java:S1452")
    Generator<?> generator() {
        ObjectSpecs<?> specs = this.config.specs(curElement, this);
        Generator<?> generator = (specs == null) ? null : specs.generator();

        if (generator != null) {
            return generator;
        }

        // even if a Generator is defined, return the default generator in case it is not compatible
        // this is a solution required while Map generators are not fully customized (key vs. value generator)
        return DefaultGenerators.generator(curElement.typeClass());
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

    public Long seed() {
        return this.seed;
    }

    public Element parent() {
        return elements.size() > 1 ? elements.get(elements.size() - 2) : null;
    }

    @SuppressWarnings("unchecked")
    public <T> CodeList<T> getCodeList(String key) {
        return (CodeList<T>) config.getCodeList(key);
    }

    public <T> void setCurrentCodeListItem(String key, CodeListItem<T> codeListItem) {
        String parentPath = XPathUtil.parent(path());
        currentCodeListItems.put(Pair.of(key, parentPath), codeListItem);
    }

    @SuppressWarnings("unchecked")
    public <T> CodeListItem<T> getCurrentCodeListItem(String key) {
        String parentPath = XPathUtil.parent(path());
        return (CodeListItem<T>) currentCodeListItems.get(Pair.of(key, parentPath));
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

