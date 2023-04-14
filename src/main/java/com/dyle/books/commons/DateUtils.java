package com.dyle.books.commons;

import org.springframework.util.Assert;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
    }

    public static ZonedDateTime fromDateToSystemDefaultZone(Date inputDate) {
        return fromDateToZone(inputDate, ZoneId.systemDefault());
    }

    public static ZonedDateTime fromDateToUTC(Date inputDate) {
        return fromDateToZone(inputDate, ZoneId.of("UTC"));
    }

    public static ZonedDateTime fromDateToZone(Date inputDate, ZoneId zoneId) {
        Assert.notNull(inputDate, () -> "inputDate must be specified");
        Assert.notNull(zoneId, () -> "inputDate must be specified");

        return inputDate.toInstant().atZone(zoneId);
    }

    public static boolean isEqual(ZonedDateTime first, ZonedDateTime second) {
        return (first == null && second == null) || (first != null && second != null && (first == second || first.isEqual(second)));
    }
}
