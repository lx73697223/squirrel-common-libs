package com.pi.common.utils.core;


import com.google.common.collect.Range;

import java.util.Comparator;

public final class ComparableUtils {

    public static <T> T min(T a, T b, Comparator<? super T> comparator) {
        if (a == null || b == null){
            return a == null ? a : b;
        }
        return comparator.compare(a, b) <= 0 ? a : b;
    }

    public static <T> T max(T a, T b, Comparator<? super T> comparator) {
        if (a == null || b == null){
            return a == null ? b : a;
        }
        return comparator.compare(a, b) >= 0 ? a : b;
    }

    /*
     * 检查 @param obj 是否包含在 @param rangeLower 和 @param rangeUpper 中.
     */
    public static <T extends Comparable<? super T>> boolean containsInculsive(T obj, T rangeLower, T rangeUpper) {
        if (obj == null || rangeLower == null || rangeUpper == null){
            return false;
        }
        return Range.closed(rangeLower, rangeUpper).contains(obj);
    }

}
