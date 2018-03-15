package io.twitch4j.impl.api.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.twitch4j.ITwitchClient;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ClientDeserialization extends JsonDeserializer<ITwitchClient> {
    private final ITwitchClient client;

    @Override
    public ITwitchClient deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        if (p.getCurrentValue() instanceof ITwitchClient)
            return (ITwitchClient) p.getCurrentValue();
        else {
            return client;
        }
    }
}
