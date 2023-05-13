package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.GuestStarLayout;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GuestStarSettings {

    /**
     * Whether Guest Star moderators have access to control whether a guest is live once assigned to a slot.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_moderator_send_live_enabled")
    private Boolean isModeratorSendLiveEnabled;

    /**
     * The number of slots the Guest Star call interface will allow the host to add to a call.
     * Required to be between 1 and 6.
     */
    private Integer slotCount;

    /**
     * Whether browser sources subscribed to sessions on this channel should output audio.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_browser_source_audio_enabled")
    private Boolean isBrowserSourceAudioEnabled;

    /**
     * How the guests within a session should be laid out within the group browser source.
     */
    private GuestStarLayout groupLayout;

    /**
     * View only token to generate browser source URLs.
     */
    private String browserSourceToken;

}
