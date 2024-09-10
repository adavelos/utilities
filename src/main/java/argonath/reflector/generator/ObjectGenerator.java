package argonath.reflector.generator;

import argonath.reflector.factory.ObjectFactory;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.reflection.TypeExplorer;
import argonath.reflector.types.iterable.IterableType;
import argonath.reflector.types.iterable.IterableTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjectGenerator {

    public static <T> Builder<T> create(Class<T> clazz) {
        return new Builder<>(GeneratorConfig.defaultConfig(), clazz);
    }

    public static <T> T generate(Class<T> clazz) {
        return generate(clazz, GeneratorConfig.defaultConfig());
    }

    public static <T> T generate(Class<T> clazz, GeneratorConfig config) {
        GeneratorContext ctx = new GeneratorContext(clazz, config);
        Object generatedObject = generate(ctx.rootElement());
        T ret = ReflectiveMutator.safeCast(generatedObject, clazz);
        return ret;
    }


    private static Object generate(GeneratorContext.Element element) {
        GeneratorContext ctx = element.ctx();
        ctx.enter(element);
        if (!ctx.doPopulate(element)) {
            ctx.exit();
            return null;
        }
        // Instantiate object
        Object ret = instantiate(element);

        // Relate with parent
        Object parent = element.parent();
        if (parent != null) {
            Field field = element.field();
            try {
                ReflectiveMutator.setFieldValue(field, parent, ret);
            } catch (Exception e) {
                if (!ctx.config().isLenient()) {
                    throw new RuntimeException("Cannot set field '" + field.getName() + "' of type: [" + field.getType().getName() + "] to value of type: [" + ret.getClass().getName() + "]");
                }
            }
        }

        ctx.exit();
        return ret;
    }

    private static Object instantiate(GeneratorContext.Element element) {
        GeneratorContext ctx = element.ctx();

        // Resolution Step 1: Value specified in Context or in Config
        Object value = ctx.value();
        if (value != null) {
            return value;
        }

        // Resolution Step 2: Generator specified in Context or in Config
        Generator<?> generator = ctx.generator();
        Object generatedValue = invokeGenerator(generator, element, ctx);
        if (generatedValue != null) {
            return generatedValue;
        }

        // Populate Iterable
        Class<?> typeClass = element.typeClass();
        if (IterableTypes.isIterableType(typeClass)) {
            IterableType iterableType = IterableTypes.iterableType(typeClass);
            return instantiateIterable(element, iterableType, ctx);
        }

        // Non-Iterable Type
        Object instantiatedValue = instantiateObject(typeClass, ctx);

        // Populate Single-Value object
        ReflectiveAccessor.getFields(typeClass)
                .forEach(f -> generate(GeneratorContext.Element.ofField(f, instantiatedValue, ctx)));

        return instantiatedValue;
    }

    private static <T> T invokeGenerator(Generator<T> generator, GeneratorContext.Element element, GeneratorContext ctx) {
        if (generator == null) {
            return null;
        }

        if (IterableTypes.isIterableType(element.typeClass()) && !IterableTypes.isIterableType(generator.type())) {
            // specs for an iterable field: generator returns a single object, so ignore the current level
            return null;
        }

        T ret = null;
        try {
            if (generator instanceof SequenceGenerator sequenceGenerator) {
                ret = invokeSequenceGenerator(sequenceGenerator, ctx);
            } else {
                ret = generator.generate(ctx.config().seed());
            }
        } catch (IllegalArgumentException e) {
            if (!ctx.config().isLenient()) {
                throw e;
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    private static <T> T invokeSequenceGenerator(SequenceGenerator generator, GeneratorContext ctx) {
        String path = ctx.path();
        String key = generator.key() != null ? generator.key() : path;
        Sequence sequence = ctx.sequence(key);
        return (T) generator.generate(ctx.seed(), sequence.next());
    }

    private static <T> T instantiateObject(Class<T> clazz, GeneratorContext ctx) {
        T ret = null;
        try {
            ret = ObjectFactory.create(clazz);
            if (ret == null && !ctx.config().isLenient()) {
                throw new IllegalArgumentException("Cannot instantiate object of type: " + clazz.getName());
            }
        } catch (IllegalArgumentException e) {
            if (!ctx.config().isLenient()) {
                throw e;
            }
        }
        return ret;
    }

    private static Object instantiateIterable(GeneratorContext.Element element, IterableType iterableType, GeneratorContext ctx) {
        // Pre-calculate size in case of Array
        int size = ctx.cardinality().randomSize();

        Collection<Object> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Type type = element.type();
            List<Object> objectList = new ArrayList<>();
            for (Type nestedType : TypeExplorer.nestedTypes(type)) {
                Object elementInstance = generate(GeneratorContext.Element.ofParam(nestedType, 0, ctx));
                objectList.add(elementInstance);
            }
            Object item = iterableType.compose(objectList);
            elements.add(item);
        }

        Object ret;
        try {
            ret = iterableType.fromCollection(elements, element.typeClass());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public static class Builder<T> {
        private GeneratorConfig config;
        private Class<T> clazz;

        public Builder(GeneratorConfig config, Class<T> clazz) {
            this.config = config;
            this.clazz = clazz;
        }

        public T generate() {
            return ObjectGenerator.generate(clazz, config);
        }

        public Builder<T> withConfig(GeneratorConfig config) {
            this.config = config;
            return this;
        }

        public Builder<T> withConfigFile(String filename) {
            this.config.withConfigFile(filename);
            return this;
        }

        public Builder<T> withCodeListFile(String filename) {
            this.config.withCodeListFile(filename);
            return this;
        }

        public Builder<T> withConfigProperty(String property, String value) {
            this.config.withConfigProperty(property, value);
            return this;
        }

        public Builder<T> withAllMandatory() {
            this.config.withAllMandatory();
            return this;
        }

        public Builder<T> withSpecs(FieldSelector selector, ObjectSpecs<?> generator) {
            this.config.withSpecs(selector, generator);
            return this;
        }

        public Builder<T> withValue(FieldSelector selector, Object value) {
            this.config.withValue(selector, value);
            return this;
        }

        public Builder<T> withSeed(Long seed) {
            this.config.withSeed(seed);
            return this;
        }

        public Builder<T> setLenient() {
            this.config.setLenient();
            return this;
        }

        public Builder<T> ignore(FieldSelector selector) {
            this.config.ignore(selector);
            return this;
        }

        public Builder<T> debug() {
            return this;
        }

    }

}
