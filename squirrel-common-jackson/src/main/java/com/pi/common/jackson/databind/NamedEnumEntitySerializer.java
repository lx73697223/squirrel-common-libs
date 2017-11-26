package com.pi.common.jackson.databind;

import java.io.IOException;

import com.pi.common.utils.enums.NamedEnumEntity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.StringUtils;

public class NamedEnumEntitySerializer extends JsonSerializer<NamedEnumEntity<?>> {

    @Override
    public void serialize(NamedEnumEntity<?> enumEntity, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("id", enumEntity.getValue());
        gen.writeStringField("name", enumEntity.getName());
        if (!StringUtils.isEmpty(enumEntity.getShortName())) {
            gen.writeStringField("shortName", enumEntity.getShortName());
        }
        gen.writeEndObject();
    }
}
