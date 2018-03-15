package io.twitch4j.impl.api.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Locale;

public class LocaleDeserializer extends JsonDeserializer<Locale> {
    @Override
    public Locale deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Locale.forLanguageTag(p.getValueAsString());
    }
}
