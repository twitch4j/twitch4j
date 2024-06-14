package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class GuestStarInvite {

    /**
     * Twitch User ID corresponding to the invited guest.
     */
    private String userId;

    /**
     * Timestamp when this user was invited to the session.
     */
    private Instant invitedAt;

    /**
     * Status representing the invited user’s join state.
     */
    private Status status;

    /**
     * Whether the invited user has chosen to disable their local audio device.
     * If user has muted themselves, they may choose to unmute their audio feed upon joining the session.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_audio_enabled")
    private Boolean isAudioEnabled;

    /**
     * Whether the invited user has chosen to disable their local video device.
     * If the user has hidden themselves, they may choose to reveal their video feed upon joining the session.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_video_enabled")
    private Boolean isVideoEnabled;

    /**
     * Whether the invited user has an audio device available for sharing.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_audio_available")
    private Boolean isAudioAvailable;

    /**
     * Whether the invited user has a video device available for sharing.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_video_available")
    private Boolean isVideoAvailable;

    public enum Status {
        /**
         * The user has been invited to the session but has not acknowledged it.
         */
        INVITED,

        /**
         * The invited user has acknowledged the invite and joined the waiting room,
         * but may still be setting up their media devices or otherwise preparing to join the call.
         */
        ACCEPTED,

        /**
         * The invited user has signaled they are ready to join the call from the waiting room.
         */
        READY
    }
}
