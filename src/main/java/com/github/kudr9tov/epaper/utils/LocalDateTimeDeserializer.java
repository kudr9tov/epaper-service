package com.github.kudr9tov.epaper.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {


    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    protected LocalDateTimeDeserializer(final Class<LocalDateTime> localDateClass) {
        super(localDateClass);
    }

    @Override
    public LocalDateTime deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        if (StringUtils.isNotBlank(p.getText())) {
            try {
                return LocalDateTime.parse(p.getText(), LocalDateTimeFormatter.FORMAT);
            } catch (DateTimeParseException e) {
                return null;
            }
        }
        return null;
    }
}