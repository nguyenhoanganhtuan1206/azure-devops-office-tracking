package com.openwt.officetracking.converter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class NullIfEmptyConverter extends JsonDeserializer<String> {

    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext context) throws IOException {
        final String input = jsonParser.getText();

        if (isBlank(input)) {
            return null;
        }

        return input;
    }
}
