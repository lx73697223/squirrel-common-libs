package com.pi.common.persistence.h2;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class Functions {

    public static Timestamp systemUTCDatetime() {
        return Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC));
    }

    public static UUID newSeqId() {
        return UUID.randomUUID();
    }

}
