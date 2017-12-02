package com.pi.common.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 This class is designed for serializing a relative URL in a DTO object into an absolute URL in the
 output serialized form. */
@JsonComponent
public abstract class UrlJsonSerializer extends JsonSerializer<String> {

    /**
     This method is exposed at the package-level for {@link UrlCollectionJsonSerializer}
     */
    static void serialize(String relativeUrl, JsonGenerator generator, String baseUrl,
                          String defaultUrl) throws IOException {

        String outputUrl = StringUtils.isEmpty(relativeUrl) ? defaultUrl : relativeUrl;

        if (StringUtils.isEmpty(outputUrl)) {
            generator.writeNull();
        } else {
            if (!StringUtils.startsWithIgnoreCase(outputUrl, "http:") &&
                !StringUtils.startsWithIgnoreCase(outputUrl, "https:")) {
                outputUrl = baseUrl.concat(outputUrl);
            }
            generator.writeString(outputUrl);
        }
    }

    /**
     @return the base URL, e.g.
     */
    protected abstract String getBaseUrl();

    /**
     @return the default URL (can be relative or absolute), or null if no default.
     */
    protected abstract String getDefaultUrl();

    @Override
    public void serialize(String relativeUrl, JsonGenerator generator,
                          SerializerProvider serializers) throws IOException {

        serialize(relativeUrl, generator, getBaseUrl(), getDefaultUrl());
    }

}
