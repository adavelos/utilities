package argonath.reflector.types.simple;

import argonath.reflector.reflection.ReflectiveAccessor;
import argonath.reflector.registry.TypeRegistry;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;

import static argonath.reflector.types.simple.BuiltInSimpleTypes.*;

public class SimpleTypes {

    private static TypeRegistry<SimpleType> registry = new TypeRegistry<>(true);

    static {
        registerBuiltInTypes();
    }

    public static void register(Class<?> type, SimpleType<?> simpleType) {
        registry.register(type, simpleType);
    }

    public static void registerBuiltInTypes() {
        register(String.class, STRING);
        register(Integer.class, INTEGER);
        register(Float.class, FLOAT);
        register(Double.class, DOUBLE);
        register(Long.class, LONG);
        register(Character.class, CHARACTER);
        register(Byte.class, BYTE);
        register(Short.class, SHORT);
        register(Boolean.class, BOOLEAN);
        register(LocalDate.class, LOCAL_DATE);
        register(LocalDateTime.class, LOCAL_DATE_TIME);
        register(OffsetDateTime.class, OFFSET_DATE_TIME);
        register(OffsetTime.class, OFFSET_TIME);
        register(ZonedDateTime.class, ZONED_DATE_TIME);
        register(BigInteger.class, BIG_INTEGER);
        register(BigDecimal.class, BIG_DECIMAL);
        register(byte[].class, BYTE_ARRAY);
    }

    public static SimpleType simpleType(Object object) {
        return simpleType(object.getClass());
    }

    public static SimpleType simpleType(Class<?> clazz) {
        return registry.match(clazz);
    }

    public static boolean isSimpleType(Class<?> clazz) {
        return simpleType(clazz) != null;
    }

    public static boolean isSimpleType(Object object) {
        return isSimpleType(object.getClass());
    }

}