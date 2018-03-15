package io.twitch4j.impl.api.kraken.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.api.kraken.model.Channel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@RequiredArgsConstructor
public class ChannelDeserialize extends JsonDeserializer<Channel> {
    private final IKraken api;

    @Override
    public Channel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return api.channelOperation().getById(p.getLongValue());
    }
}
