package argonath.utils;

public class Assert {

    private Assert() {
    }

    public static void isTrue(boolean expr, String message) {
        if (!expr) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(String value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
    }


}
