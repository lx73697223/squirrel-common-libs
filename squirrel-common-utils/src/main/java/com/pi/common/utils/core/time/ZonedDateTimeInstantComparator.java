package com.pi.common.utils.core.time;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;

/**
 * 将ZonedDateTime对象标准化到相同的时区进行比较.
 *
 * The methods ZonedDateTime.equals() and ZonedDateTime.compareTo() perform comparison by getLocalTime() and getZone():
 *   ZonedDateTime z1 = ZonedDateTime.of(2016, 12, 1, 15, 0, 0, 0, ZoneOffset.UTC).withZoneSameInstant(ZoneId.of("Asia/Shanghai"));
 *   ZonedDateTime z2 = ZonedDateTime.of(2016, 12, 1, 15, 0, 0, 0, ZoneOffset.UTC);
 *   z1.compareTo(z2); // returns 1
 *   z1.equals(z2); // returns false
 *
 *   ChronoUnit.SECONDS.between(z1, z2); // returns 0
 */
public class ZonedDateTimeInstantComparator implements Comparator<ZonedDateTime> {

    public static final ZonedDateTimeInstantComparator INSTANCE = new ZonedDateTimeInstantComparator();

    private ZonedDateTimeInstantComparator() {
    }

    @Override
    public int compare(ZonedDateTime o1, ZonedDateTime o2) {
        return compareByTimeInstant(o1, o2);
    }

    public static int compareByTimeInstant(ZonedDateTime o1, ZonedDateTime o2) {
        if (o1 != null) {
            if (o2 != null) {
                return o1.withZoneSameInstant(ZoneOffset.UTC).compareTo(o2.withZoneSameInstant(ZoneOffset.UTC));
            } else {
                return 1; // o1 != null and o2 == null
            }
        } else {
            if (o2 != null) {
                return -1; // o1 == null and o2 != null
            } else {
                return 0; // o1 == null and o2 == null
            }
        }
    }
}
