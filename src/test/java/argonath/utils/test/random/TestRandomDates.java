package argonath.utils.test.random;

import argonath.random.RandomDates;
import argonath.random.RandomNumber;
import argonath.utils.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class TestRandomDates {

    @Test
    void testRandomLocalDate() {
        for (int i = 0; i < 1000; i++) {
            int min = RandomNumber.getInteger(-1000, +1000);
            int max = RandomNumber.getInteger(min, 1000);
            LocalDate date = RandomDates.randomLocalDate(min, max);
            Assertions.assertTrue(date.isAfter(LocalDate.now().plusDays(min - 1)) &&
                            date.isBefore(LocalDate.now().plusDays(max + 1)),
                    "Date is not within the expected range");
        }

        // Edge Cases
        LocalDate date1 = RandomDates.randomLocalDate(0, 0);
        Assertions.assertTrue(date1.isEqual(LocalDate.now()), "Date is not equal to today");

        LocalDate date2 = RandomDates.randomLocalDate(100, 100);
        Assertions.assertTrue(date2.isEqual(LocalDate.now().plusDays(100)), "Date is not equal to today + 100 days");

        LocalDate date3 = RandomDates.randomLocalDate(-100, -100);
        Assertions.assertTrue(date3.isEqual(LocalDate.now().minusDays(100)), "Date is not equal to today - 100 days");

        // Negative Case
        try {
            RandomDates.randomLocalDate(0, -1);
            Assertions.assertTrue(false, "Exception not thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException, "Exception is not of type IllegalArgumentException");
        }

    }

    @Test
    void testRandomLocalDateTime() {

        for (int i = 0; i < 1000; i++) {
            int min = RandomNumber.getInteger(-1000, +1000);
            int max = RandomNumber.getInteger(min, 1000);
            LocalDateTime date = RandomDates.randomLocalDateTime(min, max);
            Assertions.assertTrue(date.isAfter(LocalDateTime.now().plusDays(min - 1)) &&
                            date.isBefore(LocalDateTime.now().plusDays(max + 1)),
                    "Date is not within the expected range");
        }


        // Edge Cases
        LocalDateTime date1 = RandomDates.randomLocalDateTime(0, 0);
        Assertions.assertTrue(date1.truncatedTo(ChronoUnit.SECONDS).isEqual(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now");

        LocalDateTime date2 = RandomDates.randomLocalDateTime(100, 100);
        Assertions.assertTrue(date2.truncatedTo(ChronoUnit.SECONDS).isEqual(LocalDateTime.now().plusDays(100).truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now + 100 days");

        LocalDateTime date3 = RandomDates.randomLocalDateTime(-100, -100);
        Assertions.assertTrue(date3.truncatedTo(ChronoUnit.SECONDS).isEqual(LocalDateTime.now().minusDays(100).truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now - 100 days");

        // Negative Case
        try {
            RandomDates.randomLocalDateTime(0, -1);
            Assertions.assertTrue(false, "Exception not thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException, "Exception is not of type IllegalArgumentException");
        }
    }

    @Test
    void testRandomOffsetDateTime() {
        for (int i = 0; i < 100; i++) {
            OffsetDateTime date = RandomDates.randomOffsetDateTime(-100, 100);
            OffsetDateTime now = offsetDateTimeNow();
            Assertions.assertTrue(date.isAfter(now.minusDays(101)) &&
                            date.isBefore(now.plusDays(101)),
                    "Date/Time is not within the expected range");
        }

        // Edge Cases
        OffsetDateTime date1 = RandomDates.randomOffsetDateTime(0, 0);
        Assertions.assertTrue(date1.truncatedTo(ChronoUnit.SECONDS).isEqual(offsetDateTimeNow().truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now");


        OffsetDateTime date2 = RandomDates.randomOffsetDateTime(100, 100);
        Assertions.assertTrue(date2.truncatedTo(ChronoUnit.SECONDS).isEqual(offsetDateTimeNow().plusDays(100).truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now + 100 days");

        OffsetDateTime date3 = RandomDates.randomOffsetDateTime(-100, -100);
        Assertions.assertTrue(date3.truncatedTo(ChronoUnit.SECONDS).isEqual(offsetDateTimeNow().minusDays(100).truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now - 100 days");

        // Negative Case
        try {
            RandomDates.randomOffsetDateTime(0, -1);
            Assertions.assertTrue(false, "Exception not thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException, "Exception is not of type IllegalArgumentException");
        }
    }

    @Test
    void testRandomOffsetTime() {
        for (int i = 0; i < 100; i++) {
            OffsetTime time = RandomDates.randomOffsetTime(-100, 100);
            OffsetTime now = offsetTimeNow();
            Assertions.assertTrue(time.isAfter(now.minusSeconds(101)) &&
                            time.isBefore(now.plusSeconds(101)),
                    "Time is not within the expected range");
        }

        // Edge Cases
        OffsetTime time1 = RandomDates.randomOffsetTime(0, 0);
        Assertions.assertTrue(time1.truncatedTo(ChronoUnit.SECONDS).isEqual(offsetTimeNow().truncatedTo(ChronoUnit.SECONDS)), "Time is not equal to now");

        OffsetTime time2 = RandomDates.randomOffsetTime(100, 100);
        Assertions.assertTrue(time2.truncatedTo(ChronoUnit.SECONDS).isEqual(offsetTimeNow().plusSeconds(100).truncatedTo(ChronoUnit.SECONDS)), "Time is not equal to now + 100 seconds");

        OffsetTime time3 = RandomDates.randomOffsetTime(-100, -100);
        Assertions.assertTrue(time3.truncatedTo(ChronoUnit.SECONDS).isEqual(offsetTimeNow().minusSeconds(100).truncatedTo(ChronoUnit.SECONDS)), "Time is not equal to now - 100 seconds");

        // Negative Case
        try {
            RandomDates.randomOffsetTime(0, -1);
            Assertions.assertTrue(false, "Exception not thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException, "Exception is not of type IllegalArgumentException");
        }
    }

    @Test
    void testRandomZonedDateTime() {
        for (int i = 0; i < 100; i++) {
            ZonedDateTime date = RandomDates.randomZonedDateTime(-100, 100);
            Assertions.assertTrue(date.isAfter(zonedDateTimeNow().minusDays(101)) &&
                            date.isBefore(zonedDateTimeNow().plusDays(101)),
                    "Date/Time is not within the expected range");
        }

        // Edge Cases
        ZonedDateTime date1 = RandomDates.randomZonedDateTime(0, 0);
        Assertions.assertTrue(date1.truncatedTo(ChronoUnit.SECONDS).isEqual(zonedDateTimeNow().truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now");

        ZonedDateTime date2 = RandomDates.randomZonedDateTime(100, 100);
        Assertions.assertTrue(date2.truncatedTo(ChronoUnit.SECONDS).isEqual(zonedDateTimeNow().plusDays(100).truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now + 100 hours");

        ZonedDateTime date3 = RandomDates.randomZonedDateTime(-100, -100);
        Assertions.assertTrue(date3.truncatedTo(ChronoUnit.SECONDS).isEqual(zonedDateTimeNow().minusDays(100).truncatedTo(ChronoUnit.SECONDS)), "Date/Time is not equal to now - 100 hours");

        // Negative Case
        try {
            RandomDates.randomZonedDateTime(0, -1);
            Assertions.assertTrue(false, "Exception not thrown");
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof IllegalArgumentException, "Exception is not of type IllegalArgumentException");
        }
    }


    private OffsetTime offsetTimeNow() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC).toOffsetTime();
    }

    private OffsetDateTime offsetDateTimeNow() {
        return LocalDateTime.now().atOffset(ZoneOffset.UTC);
    }

    private ZonedDateTime zonedDateTimeNow() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault());
    }


}
