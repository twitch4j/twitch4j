package me.philippheuer.twitch4j.events.event.pubsub.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.model.Channel;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class HostEvent extends ChatModerationEvent {
    @JsonProperty("args[0]") private Channel channel;

    public void setChannel(String channel) {
        this.channel = getClient().getChannelEndpoint(channel).getChannel();
    }
}
