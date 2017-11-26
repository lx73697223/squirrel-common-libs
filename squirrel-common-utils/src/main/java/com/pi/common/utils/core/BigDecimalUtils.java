package com.pi.common.utils.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

public final class BigDecimalUtils {

    public static final BigDecimal HUNDRED = new BigDecimal(100);

    private static final DecimalFormat BIG_DECIMAL_PARSER;

    private static final Object BIG_DECIMAL_PARSER_LOCK = new Object();

    static {
        BIG_DECIMAL_PARSER = new DecimalFormat();
        BIG_DECIMAL_PARSER.setParseBigDecimal(true);
    }

    /**
     由于DecimalFormat不是线程安全的，应该为每个线程创建一个实例。
     如果多个线程同时访问一个DecimalFormat实例，那么必须同步。
     */
    public static BigDecimal parseStringValueToBigDecimal(String value)
            throws ParseException {
        if (value != null) {
            synchronized (BIG_DECIMAL_PARSER_LOCK) {
                return (BigDecimal) BIG_DECIMAL_PARSER.parse(value);
            }
        } else {
            return null;
        }
    }

    /**
     默认向上保留两位小数
     */
    public static BigDecimal setScale(BigDecimal bigDecimal) {
        return bigDecimal == null ? null : bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertCentsToDollarsWithScale(Integer bigDecimal) {
        return bigDecimal == null ? null : BigDecimalUtils.setScale(new BigDecimal(bigDecimal).divide(HUNDRED));
    }

}
