package com.pi.common.utils.entity;

import java.util.Comparator;

import static com.pi.common.utils.core.time.ZonedDateTimeInstantComparator.compareByTimeInstant;

public class BaseEntityUtils {

    /**
     * Returns a comparator that compares two {@link BaseEntity} objects by {@link BaseEntity#getUpdatedTime()} in ascending order.
     */
    public static Comparator<BaseEntity> getUpdatedTimeComparator() {
        return UPDATED_TIME_COMPARATOR;
    }

    private static final Comparator<BaseEntity> UPDATED_TIME_COMPARATOR = new Comparator<BaseEntity>() {

        @Override
        public int compare(BaseEntity o1, BaseEntity o2) {
            if (o1 != null && o1.getUpdatedTime() != null) {
                if (o2 != null && o2.getUpdatedTime() != null) {
                    return compareByTimeInstant(o1.getUpdatedTime(), o2.getUpdatedTime());
                } else {
                    return 1;
                }
            } else {
                if (o2 != null && o2.getUpdatedTime() != null) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    };

}
