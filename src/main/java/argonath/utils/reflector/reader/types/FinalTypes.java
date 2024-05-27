package argonath.utils.reflector.reader.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Registry of Final Types with built-in support
 **/
public class FinalTypes {

    private static Map<Class<?>, FinalType<?>> FINAL_TYPES;

    private static <T> void addFinalType(Class<T> type, Function<T, String> toStringFunction) {
        FINAL_TYPES.put(type, (FinalType<T>) object -> toStringFunction.apply(object));
    }

    static {
        FINAL_TYPES = new HashMap<>();
        // add String
        addFinalType(String.class, String::toString);
        addFinalType(Integer.class, Object::toString);
        addFinalType(Float.class, Object::toString);
        addFinalType(Double.class, Object::toString);
        addFinalType(Long.class, Object::toString);
        addFinalType(Boolean.class, Object::toString);
        addFinalType(LocalDate.class, DateTime::convertLocalDateToString);
        addFinalType(LocalDateTime.class, DateTime::convertLocalDateTimeToString);
        addFinalType(OffsetDateTime.class, DateTime::convertOffsetDateTimeToString);
        addFinalType(BigDecimal.class, Numbers::bigDecimalToString);
        addFinalType(BigInteger.class, Numbers::bigIntegerToString);
    }

    public static <T> void registerFinalType(Class<T> clazz, FinalType<T> finalType) {
        FINAL_TYPES.put(clazz, finalType);
    }

    public static <T> void registerFinalType(Class<T> clazz, Function<T, String> toStringFunction) {
        FINAL_TYPES.put(clazz, (FinalType<T>) object -> toStringFunction.apply(object));
    }

    public static FinalType<?> finalType(Class<?> clazz) {
        return FINAL_TYPES.get(clazz);
    }

    public static boolean isFinalType(Class<?> clazz) {
        return FINAL_TYPES.containsKey(clazz);
    }

    public static boolean isFinalType(Object object) {
        return isFinalType(object.getClass());
    }

}
