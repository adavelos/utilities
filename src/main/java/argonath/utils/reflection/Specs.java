package argonath.utils.reflection;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Specs {
    public static Set<Class> SUPPORTED_TYPES;

    static {
        SUPPORTED_TYPES = new HashSet<>();
        SUPPORTED_TYPES.add(String.class);
        SUPPORTED_TYPES.add(Integer.class);
        SUPPORTED_TYPES.add(Float.class);
        SUPPORTED_TYPES.add(Double.class);
        SUPPORTED_TYPES.add(Long.class);
        SUPPORTED_TYPES.add(Boolean.class);
        SUPPORTED_TYPES.add(LocalDateTime.class);
        SUPPORTED_TYPES.add(LocalDate.class);
        SUPPORTED_TYPES.add(BigDecimal.class);
        SUPPORTED_TYPES.add(BigInteger.class);
        // ... other types can be added here ...

    }
}
