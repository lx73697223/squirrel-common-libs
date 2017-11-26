package com.pi.common.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pi.common.utils.enums.EnumEntity;

public class EnumEntitySerializer extends JsonSerializer<EnumEntity<?>> {

    @Override
    public void serialize(EnumEntity<?> enumEntity, JsonGenerator generator, SerializerProvider provider)
            throws IOException {
        Object value = null;
        if (enumEntity != null) {
            value = enumEntity.getValue();
        }
        generator.writeObject(value);
    }

}
