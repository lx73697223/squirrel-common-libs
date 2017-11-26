package com.pi.common.utils.core;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

public final class UUIDUtils {

    /**
     * The inclusive lower bound of UUID.
     */
    public static final BigInteger UUID_VALUE_MIN = BigInteger.ZERO;

    /**
     * The exclusive upper bound of UUID.
     */
    public static final BigInteger UUID_VALUE_MAX = BigInteger.ONE.shiftLeft(128);

    private UUIDUtils() {
    }

    public static String toHyphenlessString(UUID uuid) {
        checkArgument(uuid != null, "precondition: uuid != null");

        return uuid.toString().replace("-", "");
    }

    public static UUID fromHyphenlessString(String uuid) {
        checkArgument(StringUtils.isNotBlank(uuid), "precondition: StringUtils.isNotBlank(uuid)");

        BigInteger uuidValue = new BigInteger(uuid, 16);

        return fromBigInteger(uuidValue);
    }

    public static UUID fromBigInteger(BigInteger uuidValue) {
        checkArgument(UUID_VALUE_MIN.compareTo(uuidValue) <= 0, "precondition: UUID_MIN <= uuidAsInteger");
        checkArgument(uuidValue.compareTo(UUID_VALUE_MAX) < 0, "precondition: uuidAsInteger < UUID_MAX");

        long mostSigBits = uuidValue.shiftRight(64).longValue();
        UUID parsed = new UUID(mostSigBits, uuidValue.longValue());

        return parsed;
    }
}
