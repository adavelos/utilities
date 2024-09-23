package argonath.reflector.generator;

import argonath.reflector.factory.ObjectFactory;
import argonath.reflector.generator.model.Generator;
import argonath.reflector.generator.model.ObjectSpecs;
import argonath.reflector.generator.model.SpecsExpressionParser;
import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.reflection.TypeExplorer;
import argonath.reflector.types.iterable.IterableType;
import argonath.reflector.types.iterable.IterableTypes;
import argonath.reflector.types.simple.SimpleType;
import argonath.reflector.types.simple.SimpleTypes;
import argonath.utils.Assert;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObjectGenerator {
    private ObjectGenerator() {
    }

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
        Object obj = instantiate(element);

        // Relate with parent
        Object parent = element.parent();
        if (parent != null && obj != null) {
            Field field = element.field();
            try {
                ReflectiveMutator.setFieldValue(field, parent, obj);
            } catch (Exception e) {
                if (!ctx.config().isLenient()) {
                    String path = "Path: " + ctx.path() + " - ";
                    throw new IllegalArgumentException(path + "Cannot set field '" + field.getName() + "' of type: [" + field.getType().getName() + "] to value of type: [" + obj.getClass().getName() + "]");
                }
            }
        }

        ctx.exit();
        return obj;
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
        if (isGeneratorApplicable(generator, element)) {
            return invokeGenerator(generator, ctx);
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

    private static boolean isGeneratorApplicable(Generator<?> generator, GeneratorContext.Element element) {
        if (generator == null) {
            return false;
        }

        Assert.notNull(generator.type(), "Generator type cannot be null: " + generator.getClass());
        if (IterableTypes.isIterableType(element.typeClass()) && !IterableTypes.isIterableType(generator.type())) {
            // specs for an iterable field: generator returns a single object, so ignore current level and move deeper
            return false;
        }

        return true;
    }

    private static <T> T invokeGenerator(Generator<T> generator, GeneratorContext ctx) {
        if (generator == null) {
            return null;
        }

        T ret = null;
        try {
            ret = generator.generate(ctx);
        } catch (IllegalArgumentException e) {
            if (!ctx.config().isLenient()) {
                throw e;
            }
        }
        return ret;
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
        int size = ctx.cardinality().randomSize(ctx);

        Collection<Object> elements = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Type type = element.type();
            List<Object> objectList = new ArrayList<>();
            for (Type nestedType : TypeExplorer.nestedTypes(type)) {
                Object elementInstance = generate(GeneratorContext.Element.ofParam(nestedType, 0, ctx));
                if (elementInstance == null) {
                    continue;
                }

                // After Object Instantiation, detect if there is a String value that needs to be converted to a simple type
                if (nestedType instanceof Class<?> nestedTypeClass) {
                    SimpleType<?> simpleType = SimpleTypes.simpleType(nestedTypeClass);
                    if (simpleType != null && elementInstance instanceof String retStr) {
                        try {
                            elementInstance = simpleType.fromString(retStr);
                        } catch (Exception e) {
                            if (!ctx.config().isLenient()) {
                                String path = "Path: " + ctx.path() + " - ";
                                throw new IllegalArgumentException(path + "Cannot convert string value: [" + retStr + "] to type: [" + element.typeClass().getName() + "]");
                            }
                        }
                    }
                }

                objectList.add(elementInstance);
            }
            Object item = iterableType.compose(objectList);
            elements.add(item);
        }

        Object ret;
        try {
            ret = iterableType.fromCollection(elements, element.typeClass());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
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

        /**
         * Add a new Code List file to the configuration.
         * In case of conflict in Code List keys, the last file added will override the previous ones.
         */
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

        /**
         * Sets specs using a selector and a specs expression of the type: optionality|cardinality|generator
         */
        public Builder<T> withSpecs(FieldSelector selector, String specsExpression) {
            ObjectSpecs<T> specs = SpecsExpressionParser.parseSpec(selector.expression(), specsExpression);
            this.config.withSpecs(selector, specs);
            return this;
        }

        /**
         * Set specs using a complete expression of the type: /path/to/field=optionality|cardinality|generator
         */
        public Builder<T> withSpecs(String specsExpression) {
            Pair<String, ObjectSpecs<T>> pair = SpecsExpressionParser.parseSpec(specsExpression);
            this.config.withSpecs(FieldSelector.ofPath(pair.getLeft()), pair.getRight());
            return this;
        }

        /**
         * Load specs from a file
         */
        public Builder<T> withSpecsFile(String filename) {
            this.config.withSpecsFile(filename);
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
