package me.philippheuer.twitch4j.events.event.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import me.philippheuer.twitch4j.enums.ModerationActions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.User;

import java.util.Optional;

@Getter
@Value
@EqualsAndHashCode(callSuper = false)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModerationEvent extends Event {
    public enum Type {
        CHAT_LOGIN_MODERATION,
        CHAT_CHANNEL_MODERATION
    }

    private Type type;
    private ModerationActions moderationAction;
    @JsonProperty("target_user_id") private Optional<User> target;
    @JsonProperty("created_by_user_id") private Optional<User> createdBy;
    private String[] args;
}
