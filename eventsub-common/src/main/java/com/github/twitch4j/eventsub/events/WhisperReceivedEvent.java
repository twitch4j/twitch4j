package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.Whisper;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WhisperReceivedEvent extends EventSubUserFromToEvent {

    /**
     * The whisper ID.
     */
    private String whisperId;

    /**
     * Object containing whisper information.
     */
    private Whisper whisper;

}
