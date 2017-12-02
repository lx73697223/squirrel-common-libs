package com.pi.common.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.pi.common.utils.enums.EnumEntity;
import com.pi.common.utils.enums.EnumEntityFactory;
import com.pi.common.utils.enums.EnumEntityUtils;
import com.pi.common.utils.enums.NamedEnumEntity;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.util.StringUtils;

import java.io.IOException;

@JsonComponent
public class EnumEntityJsonComponent {

    public static class Serializer extends JsonSerializer<EnumEntity<?>> {

        @Override
        public void serialize(EnumEntity<?> enumEntity, JsonGenerator generator,
                              SerializerProvider provider) throws IOException {
            Object value = null;
            if (enumEntity != null) {
                value = enumEntity.getValue();
            }
            generator.writeObject(value);
        }
    }

    public static class NamedEnumEntitySerializer extends JsonSerializer<NamedEnumEntity<?>> {

        @Override
        public void serialize(NamedEnumEntity<?> enumEntity, JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeObjectField("id", enumEntity.getValue());
            gen.writeStringField("name", enumEntity.getName());
            if (!StringUtils.isEmpty(enumEntity.getShortName())) {
                gen.writeStringField("shortName", enumEntity.getShortName());
            }
            gen.writeEndObject();
        }
    }

    public static class Deserializer extends JsonDeserializer<EnumEntity<?>> {

        private Class<? extends EnumEntity<?>> enumEntityClass;

        private Class<?> valueType;

        public Deserializer() {
        }

        public Deserializer(Class<? extends EnumEntity<?>> enumEntityClass) {
            this.enumEntityClass = enumEntityClass;
            this.valueType = EnumEntityUtils.getEnumEntityValueType(enumEntityClass);
        }

        @Override
        public EnumEntity<?> deserialize(JsonParser parser, DeserializationContext ctxt)
                throws IOException {
            Object value = null;
            if (parser.isExpectedStartObjectToken()) {
                JsonNode node = parser.readValueAsTree();
                value = node.get("value").asText();
            } else {
                value = parser.readValueAs(valueType);
            }
            return EnumEntityFactory.getUnchecked(enumEntityClass, value);
        }
    }

}
