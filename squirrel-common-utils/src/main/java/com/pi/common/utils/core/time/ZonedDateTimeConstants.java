package com.pi.common.utils.core.time;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public final class ZonedDateTimeConstants {

    public static final ZonedDateTime AD_Y0 = ZonedDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    public static final ZonedDateTime AD_Y10K = ZonedDateTime.of(9999, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC);

    public static final ZonedDateTime EPOCH_TIME_START = ZonedDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

}
