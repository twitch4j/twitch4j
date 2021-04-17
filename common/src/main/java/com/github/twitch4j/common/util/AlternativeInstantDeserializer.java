package com.github.twitch4j.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

public class AlternativeInstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String str = p.getValueAsString();

        if (str == null)
            return null;

        str = str.trim();

        if (str.endsWith(" +0000 UTC"))
            str = str.substring(0, str.length() - " +0000 UTC".length()) + "Z";

        str = str.replace(' ', 'T');

        return Instant.parse(str);
    }
}
