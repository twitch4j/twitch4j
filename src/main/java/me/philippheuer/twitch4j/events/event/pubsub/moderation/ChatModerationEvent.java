package me.philippheuer.twitch4j.events.event.pubsub.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public abstract class ChatModerationEvent extends Event {
    private Channel channel;
    @JsonProperty("created_by_user_id") private User createdByUser;
    @JsonProperty("target_user_id") private Optional<User> targetUser = Optional.empty();

    public void setCreatedByUser(String id) {
        this.createdByUser = getClient().getUserEndpoint().getUser(Long.parseLong(id)).get();
    }

    public void setTargetUser(String id) {
        if (id.length() > 0)
            this.targetUser = getClient().getUserEndpoint().getUser(Long.parseLong(id));
    }
}
