package argonath.reflector.generator;

import argonath.reflector.generator.model.Generator;
import argonath.reflector.reflection.Primitives;
import argonath.reflector.reflection.ReflectiveMutator;
import argonath.reflector.registry.TypeRegistry;

public class GeneratorRegistry {

    private static TypeRegistry<Generator<?>> registry = new TypeRegistry<>(false);

    public static void register(Class<?> type, Generator<?> supplier) {
        registry.register(type, supplier);
    }

    static {
        registerBuiltInTypes();
    }

    public static Generator<?> generator(Class<?> type, String path) {
        if (type.isPrimitive()) {
            type = Primitives.getWrapperType(type);
        }
        Generator<?> generator = ReflectiveMutator.safeCast(registry.match(type), Generator.class);
        return generator;
    }

    private static void registerBuiltInTypes() {
        // String
        registry.register(java.lang.String.class, Generators.randomString("an5..10"));

        // Number
        registry.register(java.lang.Integer.class, Generators.randomInt(1, 100));
        registry.register(java.lang.Short.class, Generators.randomShort((short) 1, (short) 100));
        registry.register(java.lang.Byte.class, Generators.randomByte((byte) 1, (byte) 100));
        registry.register(java.lang.Character.class, Generators.randomCharacter((char) 1, (char) 100));
        registry.register(java.lang.Long.class, Generators.randomLong(1, 100));
        registry.register(java.lang.Double.class, Generators.randomDouble(1, 100));
        registry.register(java.lang.Float.class, Generators.randomFloat(1, 100));

        // Big Number
        registry.register(java.math.BigInteger.class, Generators.randomBigInteger(1, 100));
        registry.register(java.math.BigDecimal.class, Generators.randomBigDecimal(1, 100));

        // Boolean
        registry.register(java.lang.Boolean.class, Generators. randomBoolean());

        // Dates
        registry.register(java.time.LocalDate.class, Generators.randomLocalDate(-100, 100));
        registry.register(java.time.LocalDateTime.class, Generators.randomLocalDateTime(-100, 100));
        registry.register(java.time.OffsetDateTime.class, Generators.randomOffsetDateTime(-100, 100));
        registry.register(java.time.OffsetTime.class, Generators.randomOffsetTime(-100, 100));
        registry.register(java.time.ZonedDateTime.class, Generators.randomZonedDateTime(-100, 100));

        registry.register(byte[].class, Generators.randomByteArray(1, 100));
    }
}
