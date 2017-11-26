package com.pi.common.jackson.databind;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public abstract class UrlCollectionJsonSerializer extends JsonSerializer<Iterable<String>> {

    /**
     @return the base URL, e.g.
     */
    protected abstract String getBaseUrl();

    /**
     @return the default URL (can be relative or absolute), or null if no default.
     */
    protected abstract String getDefaultUrl();

    @Override
    public void serialize(Iterable<String> values, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {

        if (values == null) {
            generator.writeNull();
            return;
        }

        String baseUrl = getBaseUrl();
        String defaultUrl = getDefaultUrl();

        generator.writeStartArray();
        for (String value : values) {
            UrlJsonSerializer.serialize(value, generator, baseUrl, defaultUrl);
        }
        generator.writeEndArray();
    }

}
