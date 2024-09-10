package argonath.reflector.types.builtin;

import argonath.reflector.config.Configuration;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTime {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String DEFAULT_OFFSET_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final String DEFAULT_OFFSET_TIME_FORMAT = "HH:mm:ssXXX";

    private static final String DEFAULT_ZONED_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";

    public static String toString(LocalDate date) {
        String dateFormat = Configuration.dateFormat(DEFAULT_DATE_FORMAT);
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String toString(LocalDateTime dateTime) {
        String dateTimeFormat = Configuration.dateTimeFormat(DEFAULT_DATE_TIME_FORMAT);
        return dateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static String toString(OffsetDateTime offsetDateTime) {
        String offsetDateTimeFormat = Configuration.offsetDateTimeFormat(DEFAULT_OFFSET_DATE_TIME_FORMAT);
        return offsetDateTime.format(DateTimeFormatter.ofPattern(offsetDateTimeFormat));
    }

    public static String toString(OffsetTime offsetTime) {
        String offsetTimeFormat = Configuration.offsetTimeFormat(DEFAULT_OFFSET_TIME_FORMAT);
        return offsetTime.format(DateTimeFormatter.ofPattern(offsetTimeFormat));
    }

    public static String toString(ZonedDateTime zonedDateTime) {
        String dateTimeFormat = Configuration.zonedDateTimeFormat(DEFAULT_ZONED_DATE_TIME_FORMAT);
        return zonedDateTime.format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalDate fromStringToDate(String date) {
        String dateFormat = Configuration.dateFormat(DEFAULT_DATE_FORMAT);
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDateTime fromStringToDateTime(String dateTime) {
        String dateTimeFormat = Configuration.dateTimeFormat(DEFAULT_DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static OffsetDateTime fromStringToOffsetDateTime(String offsetDateTime) {
        String offsetDateTimeFormat = Configuration.offsetDateTimeFormat(DEFAULT_OFFSET_DATE_TIME_FORMAT);
        return OffsetDateTime.parse(offsetDateTime, DateTimeFormatter.ofPattern(offsetDateTimeFormat));
    }

    public static OffsetTime fromStringToOffsetTime(String offsetTime) {
        String offsetTimeFormat = Configuration.offsetTimeFormat(DEFAULT_OFFSET_TIME_FORMAT);
        return OffsetTime.parse(offsetTime, DateTimeFormatter.ofPattern(offsetTimeFormat));
    }

    public static ZonedDateTime fromStringToZonedDateTime(String zonedDateTime) {
        String dateTimeFormat = Configuration.zonedDateTimeFormat(DEFAULT_ZONED_DATE_TIME_FORMAT);
        return ZonedDateTime.parse(zonedDateTime, DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}
