package me.philippheuer.twitch4j.events.event.pubsub.playback;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;

import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public abstract class PlaybackEvent extends Event {
    private Calendar serverTime;
    private Channel channel;
}
