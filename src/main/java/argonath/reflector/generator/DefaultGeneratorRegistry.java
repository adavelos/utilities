package argonath.reflector.generator;

import argonath.reflector.generator.model.Generator;
import argonath.reflector.reflection.Primitives;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.registry.TypeRegistry;

public class DefaultGeneratorRegistry {

    private static TypeRegistry<Generator<?>> defaultGeneratorRegistry = new TypeRegistry<>(false);

    public static void registerDefaultGenerator(Class<?> type, Generator<?> supplier) {
        defaultGeneratorRegistry.register(type, supplier);
    }

    static {
        registerDefaultGenerators();
    }

    public static Generator<?> generator(Class<?> type) {
        if (type.isPrimitive()) {
            type = Primitives.getWrapperType(type);
        }
        Generator<?> generator = ReflectiveMutator.safeCast(defaultGeneratorRegistry.match(type), Generator.class);
        return generator;
    }

    private static void registerDefaultGenerators() {
        // String
        defaultGeneratorRegistry.register(java.lang.String.class, Generators.randomString("an5..10"));

        // Number
        defaultGeneratorRegistry.register(java.lang.Integer.class, Generators.randomInt(1, 100));
        defaultGeneratorRegistry.register(java.lang.Short.class, Generators.randomShort((short) 1, (short) 100));
        defaultGeneratorRegistry.register(java.lang.Byte.class, Generators.randomByte((byte) 1, (byte) 100));
        defaultGeneratorRegistry.register(java.lang.Character.class, Generators.randomCharacter('A', 'z'));
        defaultGeneratorRegistry.register(java.lang.Long.class, Generators.randomLong(1, 100));
        defaultGeneratorRegistry.register(java.lang.Double.class, Generators.randomDouble(1, 100));
        defaultGeneratorRegistry.register(java.lang.Float.class, Generators.randomFloat(1, 100));

        // Big Number
        defaultGeneratorRegistry.register(java.math.BigInteger.class, Generators.randomBigInteger(1, 100));
        defaultGeneratorRegistry.register(java.math.BigDecimal.class, Generators.randomBigDecimal(1, 100));

        // Boolean
        defaultGeneratorRegistry.register(java.lang.Boolean.class, Generators.randomBoolean());

        // Dates
        defaultGeneratorRegistry.register(java.time.LocalDate.class, Generators.randomLocalDate(-100, 100));
        defaultGeneratorRegistry.register(java.time.LocalDateTime.class, Generators.randomLocalDateTime(-100, 100));
        defaultGeneratorRegistry.register(java.time.OffsetDateTime.class, Generators.randomOffsetDateTime(-100, 100));
        defaultGeneratorRegistry.register(java.time.OffsetTime.class, Generators.randomOffsetTime(-100, 100));
        defaultGeneratorRegistry.register(java.time.ZonedDateTime.class, Generators.randomZonedDateTime(-100, 100));

        defaultGeneratorRegistry.register(byte[].class, Generators.randomByteArray(1, 100));
    }
}
