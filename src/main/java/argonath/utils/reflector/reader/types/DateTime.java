package argonath.utils.reflector.reader.types;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class DateTime {
    private static String dateFormat = "dd-MM-yyyy";
    private static String dateTimeFormat = "dd-MM-yyyy HH:mm:ss";

    public static String convertLocalDateToString(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(java.time.format.DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String convertLocalDateTimeToString(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(java.time.format.DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static String convertOffsetDateTimeToString(OffsetDateTime date) {
        if (date == null) {
            return null;
        }
        return date.format(java.time.format.DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
