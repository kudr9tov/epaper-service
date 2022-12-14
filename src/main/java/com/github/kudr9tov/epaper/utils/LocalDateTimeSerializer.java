package com.github.kudr9tov.epaper.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    protected LocalDateTimeSerializer(final Class<LocalDateTime> localDateClass) {
        super(localDateClass);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider sp) throws IOException {
        gen.writeString(value.format(LocalDateTimeFormatter.FORMAT));
    }
}