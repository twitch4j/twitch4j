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
public class SlowModeEvent extends ChatModerationEvent {
    @JsonProperty("args[0]") private int time = 120;
    private boolean active;

    public void setTime(String seconds) {
        if (active) this.time = Integer.parseInt(seconds);
        else this.time = -1;
    }
}
