package com.pi.common.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityFactory;
import com.pi.common.utils.enums.EnumEntityUtils;

public class EnumEntityDeserializer extends JsonDeserializer<EnumEntity<?>> {

    private Class<? extends EnumEntity<?>> enumEntityClass;

    private Class<?> valueType;

    public EnumEntityDeserializer(Class<? extends EnumEntity<?>> enumEntityClass) {
        this.enumEntityClass = enumEntityClass;
        this.valueType = EnumEntityUtils.getEnumEntityValueType(enumEntityClass);
    }

    @Override
    public EnumEntity<?> deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException {
        Object value = null;
        if (parser.isExpectedStartObjectToken()) {
            JsonNode node = parser.readValueAsTree();
            value = node.get("id").asText();
        } else {
            value = parser.readValueAs(valueType);
        }
        return EnumEntityFactory.getUnchecked(enumEntityClass, value);
    }

}
