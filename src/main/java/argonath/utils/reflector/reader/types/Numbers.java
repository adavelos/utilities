package argonath.utils.reflector.reader.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Numbers {
    public static String bigDecimalToString(BigDecimal t) {
        if (t == null) {
            return null;
        }
        return t.toString();
    }

    public static String bigIntegerToString(BigInteger t) {
        if (t == null) {
            return null;
        }
        return t.toString();
    }
}
