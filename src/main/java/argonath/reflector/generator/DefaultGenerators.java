package argonath.reflector.generator;

import argonath.reflector.generator.model.Generator;
import argonath.reflector.reflection.Primitives;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.registry.TypeRegistry;

public class DefaultGenerators {
    private DefaultGenerators() {
    }

    private static TypeRegistry<Generator<?>> defaultGeneratorsRegistry = new TypeRegistry<>(false);

    public static void registerDefaultGenerator(Class<?> type, Generator<?> supplier) {
        defaultGeneratorsRegistry.register(type, supplier);
    }

    static {
        registerDefaultGenerators();
    }

    @SuppressWarnings("java:S1452")
    public static Generator<?> generator(Class<?> type) {
        if (type.isPrimitive()) {
            type = Primitives.getWrapperType(type);
        }
        Generator<?> generator = ReflectiveMutator.safeCast(defaultGeneratorsRegistry.match(type), Generator.class);
        return generator;
    }

    private static void registerDefaultGenerators() {
        // String
        defaultGeneratorsRegistry.register(java.lang.String.class, Generators.randomString("an5..10"));

        // Number
        defaultGeneratorsRegistry.register(java.lang.Integer.class, Generators.randomInt(1, 100));
        defaultGeneratorsRegistry.register(java.lang.Short.class, Generators.randomShort((short) 1, (short) 100));
        defaultGeneratorsRegistry.register(java.lang.Byte.class, Generators.randomByte((byte) 1, (byte) 100));
        defaultGeneratorsRegistry.register(java.lang.Character.class, Generators.randomCharacter('A', 'z'));
        defaultGeneratorsRegistry.register(java.lang.Long.class, Generators.randomLong(1, 100));
        defaultGeneratorsRegistry.register(java.lang.Double.class, Generators.randomDouble(1, 100));
        defaultGeneratorsRegistry.register(java.lang.Float.class, Generators.randomFloat(1, 100));

        // Big Number
        defaultGeneratorsRegistry.register(java.math.BigInteger.class, Generators.randomBigInteger(1, 100));
        defaultGeneratorsRegistry.register(java.math.BigDecimal.class, Generators.randomBigDecimal(1, 100));

        // Boolean
        defaultGeneratorsRegistry.register(java.lang.Boolean.class, Generators.randomBoolean());

        // Dates
        defaultGeneratorsRegistry.register(java.time.LocalDate.class, Generators.randomLocalDate(-100, 100));
        defaultGeneratorsRegistry.register(java.time.LocalDateTime.class, Generators.randomLocalDateTime(-100, 100));
        defaultGeneratorsRegistry.register(java.time.OffsetDateTime.class, Generators.randomOffsetDateTime(-100, 100));
        defaultGeneratorsRegistry.register(java.time.OffsetTime.class, Generators.randomOffsetTime(-100, 100));
        defaultGeneratorsRegistry.register(java.time.ZonedDateTime.class, Generators.randomZonedDateTime(-100, 100));

        defaultGeneratorsRegistry.register(byte[].class, Generators.randomByteArray(1, 100));
    }
}
