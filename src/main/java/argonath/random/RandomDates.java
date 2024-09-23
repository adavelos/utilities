package argonath.random;

import argonath.utils.Assert;

import java.time.*;

public class RandomDates {
    private RandomDates() {
    }

    /**
     * Generate a random date between minusDays and plusDays days from now.
     */
    public static LocalDate randomLocalDate(int minusDays, int plusDays) {
        Assert.isTrue(minusDays <= plusDays, "Invalid Argument: Min date is after Max date");
        Integer days = RandomNumber.getInteger(minusDays, plusDays + 1);
        return LocalDate.now().plusDays(days);
    }

    /**
     * Generate a random date between minusDays and plusDays days from now.
     * Note: The resolution of the randomness is in seconds.
     */
    public static LocalDateTime randomLocalDateTime(int minusDays, int plusDays) {
        Assert.isTrue(minusDays <= plusDays, "Invalid Argument: Min date is after Max date");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDate = now.plusDays(minusDays);
        LocalDateTime maxDate = now.plusDays(plusDays);
        long seconds = Duration.between(minDate, maxDate).getSeconds();
        long shift = RandomNumber.getLong(0L, seconds + 1);
        return minDate.plusSeconds(shift);
    }

    /**
     * Generate a random date between minusDays and plusDays days from now.
     * Note: The resolution of the randomness is in seconds.
     */
    public static OffsetDateTime randomOffsetDateTime(int minusDays, int maxDays) {
        Assert.isTrue(minusDays <= maxDays, "Invalid Argument: Min date is after Max date");
        return randomLocalDateTime(minusDays, maxDays).atOffset(ZoneOffset.UTC);
    }

    /**
     * Generate a random time (with offset) between startOffsetSeconds and endOffsetSeconds hours from now.
     */
    public static OffsetTime randomOffsetTime(int startOffsetSeconds, int endOffsetSeconds) {
        Assert.isTrue(startOffsetSeconds <= endOffsetSeconds, "Invalid Argument: Min value is after Max value");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minDate = now.plusSeconds(startOffsetSeconds);
        LocalDateTime maxDate = now.plusSeconds(endOffsetSeconds);
        long seconds = Duration.between(minDate, maxDate).getSeconds();
        long shift = RandomNumber.getLong(0L, seconds + 1);
        return minDate.plusSeconds(shift).atOffset(ZoneOffset.UTC).toOffsetTime();
    }

    /**
     * Generate a random date between minHours and maxHours hours from now.
     * Note: The resolution of the randomness is in seconds.
     */
    public static ZonedDateTime randomZonedDateTime(int minHours, int maxHours) {
        Assert.isTrue(minHours <= maxHours, "Invalid Argument: Min date is after Max date");
        return randomLocalDateTime(minHours, maxHours).atZone(ZoneId.systemDefault());
    }
}
