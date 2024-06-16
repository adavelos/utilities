package argonath.utils.reflector.generator;

import argonath.utils.random.RandomNumber;
import argonath.utils.reflection.ReflectiveAccessor;

import java.lang.reflect.Field;

public class ObjectGenerator {

    public static <T> T generate(Class<T> clazz, GeneratorConfig config) {
        GeneratorContext ctx = new GeneratorContext(config);
        T ret = populate(null, null, clazz, ctx);
        return null;
    }

    private static <T> T populate(Object parent, Field field, Class<T> clazz, GeneratorContext ctx) {
        T ret = null;
        boolean isRoot = (parent == null);
        boolean doPopulate =
                isRoot ||
                        (ctx.strategy() == GeneratorStrategy.ALL) ||
                        (ctx.strategy() == GeneratorStrategy.RANDOM && RandomNumber.throwDice()) ||
                        (ctx.strategy() == GeneratorStrategy.NONE && ctx.hasSpecs()) ||
                        (ctx.strategy() == GeneratorStrategy.NONE && ctx.hasValue());

        if (!doPopulate) {
            return null;
        }

        // instantiate object
        instantiateObject(clazz, ctx);

        // relate with parent
        if (parent != null) {
            field.setAccessible(true);
            try {
                field.set(parent, ret);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot Set Object value of Class: " + ret.getClass() + " in path: " + ctx.path() + " to Class: " + ret.getClass().getName(), e);
            }
        }

        return ret;
    }

    private static <T> T instantiateObject(Class<T> clazz, GeneratorContext ctx) {
        T ret = null;

        // Generate based on fixed value
        if (ctx.hasValue()) {
            Object value = ctx.value();
            if (!clazz.isAssignableFrom(value.getClass())) {
                throw new RuntimeException("Value in path " + ctx.path() + " is not assignable to type " + clazz.getName());
            }
            return clazz.cast(value);
        }

        if (ReflectiveAccessor.isCollection(clazz)) {
            return instantiateCollection(clazz, ctx);
        } else if (ReflectiveAccessor.isMap(clazz)) {
            return instantiateMap(clazz, ctx);
        } else if (ReflectiveAccessor.isArray(clazz)) {
            return instantiateArray(clazz, ctx);
        } else if (ReflectiveAccessor.isEnum(clazz)) {
            return instantiateSimpleType(clazz, ctx);
        } else if (ReflectiveAccessor.is(clazz)) {
            return instantiateSimpleType(clazz, ctx);
        } else {
            return instantiateComplexType(clazz, ctx);
        }

        // Generate based on specs
        if (ctx.hasSpecs()) {
            String specs = ctx.specs();
            // custom generator OR specs
            throw new UnsupportedOperationException("Not implemented yet");
        }

        // Generate Random Value
        ret = generateValue(clazz);

        return null;
    }

    private static <T> T generateValue(Class<T> clazz) {
        return null;
    }

    public static <T> GeneratorConfig<T> config(Class<T> clazz) {
        return new GeneratorConfig<T>(clazz, GeneratorStrategy.NONE);
    }

    public static <T> GeneratorConfig<T> config(Class<T> clazz, GeneratorStrategy strategy) {
        return new GeneratorConfig<T>(clazz, strategy);
    }

    static class Element {
        String name;

        public Element(String name) {
            this.name = name;
        }

        public String name() {
            return name;
        }
    }
}
