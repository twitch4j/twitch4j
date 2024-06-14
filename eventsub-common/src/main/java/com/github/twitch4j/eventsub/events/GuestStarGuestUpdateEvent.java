package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.github.twitch4j.eventsub.domain.GuestStarState;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GuestStarGuestUpdateEvent extends ChannelGuestStarSlotEvent {

    /**
     * ID representing the unique session that was started.
     */
    private String sessionId;

    /**
     * The current state of the user after the update has taken place, or null if the slot is now empty.
     */
    private GuestStarState state;

    /**
     * Whether the host is allowing the slot’s video to be seen by participants within the session,
     * or null if the guest is not slotted.
     */
    @Nullable
    @JsonAlias("is_host_video_enabled")
    private Boolean hostVideoEnabled;

    /**
     * Whether the host is allowing the slot’s audio to be heard by participants within the session,
     * or null if the guest is not slotted.
     */
    @Nullable
    @JsonAlias("is_host_audio_enabled")
    private Boolean hostAudioEnabled;

    /**
     * Value between 0-100 that represents the slot’s audio level as heard by participants within the session,
     * or null if the guest is not slotted.
     */
    @Nullable
    private Integer hostVolume;

}
