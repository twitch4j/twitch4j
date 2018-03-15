package io.twitch4j.api.kraken.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.api.model.IIDModel;
import io.twitch4j.api.model.Model;
import io.twitch4j.impl.api.kraken.model.ChatRoomBuilder;

@JsonDeserialize(builder = ChatRoomBuilder.class)
public abstract class ChatRoom extends Model implements IIDModel<String> {
    public abstract Channel getOwner();

    public abstract String getName();

    public abstract String getTopic();

    @JsonProperty("is_previewable")
    public abstract boolean isPreviewable();

    public abstract AccessRole getMinimumAllowedRole();

    public enum AccessRole {
        EVERYONE,
        SUBSCRIBER,
        MODERATOR
    }
}
