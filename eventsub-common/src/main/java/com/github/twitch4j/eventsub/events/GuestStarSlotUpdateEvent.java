package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GuestStarSlotUpdateEvent extends ChannelGuestStarSlotEvent {

    /**
     * ID representing the unique session the event took place in.
     */
    private String sessionId;

    /**
     * Whether the host is allowing the slot’s video to be seen by participants within the session.
     */
    private Boolean hostVideoEnabled;

    /**
     * Whether the host is allowing the slot’s audio to be heard by participants within the session.
     */
    private Boolean hostAudioEnabled;

    /**
     * Value between 0-100 that represents the slot’s audio level as heard by participants within the session.
     */
    private Integer hostVolume;

}
