package me.philippheuer.twitch4j.events.event.pubsub.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TimeoutEvent extends ChatModerationEvent {
    @JsonProperty("args[1]") private int time;
    @JsonProperty("args[2]") private String reason;

    public void setTime(String seconds) {
        this.time = Integer.parseInt(seconds);
    }
}
