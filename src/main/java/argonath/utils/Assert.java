package argonath.utils;

public class Assert {
    public static void isTrue(boolean expr, String message) {
        if (!expr) {
            throw new RuntimeException(message);
        }
    }

    public static void notNull(String value, String message) {
        if (value == null) {
            throw new RuntimeException(message);
        }
    }
}
