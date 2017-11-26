package com.pi.common.utils.mapper.bean;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.PassThroughConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

public class BeanMapper {

    private static final MapperFacade MAPPER = createDefaultMapperFactory().getMapperFacade();

    public static MapperFactory createDefaultMapperFactory() {

        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory = mapperFactory.getConverterFactory();
        converterFactory.registerConverter(new PassThroughConverter(Instant.class, LocalDate.class, LocalDateTime.class,
                LocalTime.class, OffsetDateTime.class, OffsetTime.class, ZonedDateTime.class, Year.class, YearMonth.class));
        converterFactory.registerConverter(new EnumEntityConverter());
        converterFactory.registerConverter(new EnumEntityMappingConverter());

        return mapperFactory;
    }

    public static <S, D> D map(S source, Class<D> destinationClass) {
        return MAPPER.map(source, destinationClass);
    }

    public static <S, D> List<D> mapList(Iterable<S> sourceList, Class<D> destinationClass) {
        return MAPPER.mapAsList(sourceList, destinationClass);
    }
}
