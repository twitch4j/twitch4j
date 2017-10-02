package me.philippheuer.twitch4j.events.event.pubsub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BitsEvent {
    @JsonProperty("user_id") private User user;
    @JsonProperty("channel_id") private Channel channel;
    @JsonProperty("chat_message") private String message;
    private Calendar time;
    private long bitsUsed;
    private long totalBitsUsed;
}
