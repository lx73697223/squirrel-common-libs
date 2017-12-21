package com.pi.common.utils.entity;

import java.time.LocalTime;
import java.time.ZonedDateTime;

import org.apache.commons.lang3.StringUtils;

import com.pi.common.utils.core.time.TimeUtils;

import lombok.Data;

@Data
public class BaseRangeEntity {

    /** 起始时间, UTC时间, 格式: 2017-11-29T00:00:00Z */
    private String start;

    /** 终止时间, UTC时间, 格式: 2017-11-29T23:59:59Z */
    private String end;

    public ZonedDateTime getStartTime() {
        ZonedDateTime startTime = StringUtils.isBlank(start)
                ? ZonedDateTime.of(TimeUtils.nowLocalDate(), LocalTime.MIN, TimeUtils.DEFAULT_ZONE) : ZonedDateTime.parse(start);
        return startTime;
    }

    public ZonedDateTime getEndTime() {
        ZonedDateTime endTime = StringUtils.isBlank(end)
                ? ZonedDateTime.of(TimeUtils.nowLocalDate(), LocalTime.MAX, TimeUtils.DEFAULT_ZONE) : ZonedDateTime.parse(end);
        return endTime;
    }

}
