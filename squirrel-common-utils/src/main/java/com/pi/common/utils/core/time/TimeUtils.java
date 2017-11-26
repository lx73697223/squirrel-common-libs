package com.pi.common.utils.core.time;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class TimeUtils {

    private static final ConcurrentMap<String, DateTimeFormatter> CACHE = new ConcurrentHashMap<>();

    public static DateTimeFormatter ofPattern(String format) {
        return CACHE.computeIfAbsent(format, DateTimeFormatter::ofPattern);
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        if (date == null) {
            return null;
        }

        // The old Date type is really just a 64bit number since 1970/01/01 00:00:00 GMT.
        // It has no information about time zone; the time zone you see from Date.toString() is just the system's default zone.
        long epochMilli = date.getTime();
        return Instant.ofEpochMilli(epochMilli).atZone(ZoneOffset.UTC);
    }

    /**
     * Returns the specified zonedDateTime in the old {@link Date} type
     */
    public static Date toDate(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return null;
        }
        return Date.from(zonedDateTime.toInstant());
    }

}
