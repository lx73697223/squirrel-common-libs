package com.pi.common.utils.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Data
public class BaseRangeEntity {

    /**
     起始时间, UTC时间, 格式: 2017-11-29T00:00:00Z
     */
    private String start;

    /**
     终止时间, UTC时间, 格式: 2017-11-29T23:59:59Z
     */
    private String end;

    @JsonIgnore
    public ZonedDateTime getStartTime() {
        ZonedDateTime startTime = StringUtils.isBlank(start)
                ? ZonedDateTime.of(ZonedDateTime.now(ZoneOffset.UTC).toLocalDate(), LocalTime.MIN, ZoneOffset.UTC)
                : ZonedDateTime.parse(start);
        return startTime;
    }

    @JsonIgnore
    public ZonedDateTime getEndTime() {
        ZonedDateTime endTime = StringUtils.isBlank(end)
                ? ZonedDateTime.of(ZonedDateTime.now(ZoneOffset.UTC).toLocalDate(), LocalTime.MAX, ZoneOffset.UTC)
                : ZonedDateTime.parse(end);
        return endTime;
    }

}
