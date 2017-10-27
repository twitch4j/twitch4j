package me.philippheuer.twitch4j.events.event.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import me.philippheuer.twitch4j.model.ChatEmote;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public abstract class PubSubChatEvent extends Event {
    @JsonProperty("channel_id") private Channel channel;
    @JsonProperty("user_id") private User user;
    private Calendar time;
    private Message message;

    public void setChannel(long id) {
        this.channel = getClient().getChannelEndpoint(id).getChannel();
    }

    public void setUser(long id) {
        this.getClient().getUserEndpoint().getUser(id).get();
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public class Message {
        private String message;
        private ChatEmote[] emotes;
    }
}
