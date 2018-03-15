package io.twitch4j.impl.api.kraken;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.twitch4j.ITwitchClient;
import io.twitch4j.annotation.Unofficial;
import io.twitch4j.api.ApiException;
import io.twitch4j.api.kraken.IKraken;
import io.twitch4j.api.kraken.model.*;
import io.twitch4j.api.kraken.operations.*;
import io.twitch4j.api.model.ErrorResponse;
import io.twitch4j.auth.ICredential;
import io.twitch4j.impl.api.ApiImpl;
import io.twitch4j.impl.api.deserializers.ClientDeserialization;
import io.twitch4j.impl.api.deserializers.LocaleDeserializer;
import io.twitch4j.impl.api.kraken.deserializers.ChannelDeserialize;
import io.twitch4j.impl.api.kraken.deserializers.UserDeserialize;
import io.twitch4j.impl.api.kraken.deserializers.VideoDeserialize;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;

import java.util.List;
import java.util.Locale;

public class KrakenImpl extends ApiImpl implements IKraken {
    private final ObjectMapper mapper;

    public KrakenImpl(ITwitchClient client) {
        super(client);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Locale.class, new LocaleDeserializer());
        module.addDeserializer(ITwitchClient.class, new ClientDeserialization(client));
        module.addDeserializer(User.class, new UserDeserialize(this));
        module.addDeserializer(Channel.class, new ChannelDeserialize(this));
        module.addDeserializer(Video.class, new VideoDeserialize(this));
        this.mapper = Json.mapper;
        this.mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }

    @Override
    public <T> T buildPOJO(HttpResponse<Buffer> response, TypeReference<T> typeResponse) throws RuntimeException {
        if (response.statusCode() >= 400) {
            throw new ApiException(response.bodyAsJson(ErrorResponse.class));
        } else if (response.statusCode() < 300) {
            JsonObject jsonResponse = response.bodyAsJson(JsonObject.class);
            return mapper.convertValue(jsonResponse.getMap(), typeResponse);
        } else return null;
    }

    @Override
    public Feeds feedOpration() {
        return null;
    }

    @Override
    public Channels channelOperation() {
        return null;
    }

    @Override
    public Chat chatOperation() {
        return null;
    }

    @Override
    public Clips clipOpration() {
        return null;
    }

    @Override
    public Collections collectionOperation() {
        return null;
    }

    @Override
    public Communities communityOperation() {
        return null;
    }

    @Override
    public Games gameOperation() {
        return null;
    }

    @Override
    public List<IngestServer> getServerList() {
        return null;
    }

    @Override
    public Streams streamOperation() {
        return null;
    }

    @Override
    public Teams teamOperation() {
        return null;
    }

    @Override
    @Unofficial
    public Undocumented undocumentedOperation() {
        return null;
    }

    @Override
    public Users userOperation() {
        return null;
    }

    @Override
    public Videos videoOperation() {
        return null;
    }

    @Override
    public Kraken fetchUserInfo(ICredential credential) {
        return null;
    }
}
