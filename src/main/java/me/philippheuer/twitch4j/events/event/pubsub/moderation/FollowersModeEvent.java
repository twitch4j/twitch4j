package me.philippheuer.twitch4j.events.event.pubsub.moderation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import me.philippheuer.util.conversion.Duration;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.TimeUnit;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FollowersModeEvent extends ChatModerationEvent {
    @JsonProperty("args[0]") private long time = -1L;
    private boolean active;

    public void setTime(String time) {
        if (active) {
            if (time.length() == 0) this.time = 0L;
            else if (time.matches("^[0-9]+$"))
                this.time = new Duration(Long.parseLong(time), Duration.Unit.MINUTES).convert(TimeUnit.SECONDS);
            else this.time = new Duration(time).convert(TimeUnit.SECONDS);
        } else this.time = -1L;
    }
}
