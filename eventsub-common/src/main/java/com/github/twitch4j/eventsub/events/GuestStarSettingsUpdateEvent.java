package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.GuestStarLayout;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GuestStarSettingsUpdateEvent extends EventSubChannelEvent {

    /**
     * Whether Guest Star moderators have access to control whether a guest is live once assigned to a slot.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_moderator_send_live_enabled")
    private Boolean isModeratorSendLiveEnabled;

    /**
     * The number of slots the Guest Star call interface will allow the host to add to a call.
     */
    private Integer slotCount;

    /**
     * Whether browser sources subscribed to sessions on this channel should output audio
     */
    @Accessors(fluent = true)
    @JsonProperty("is_browser_source_audio_enabled")
    private Boolean isBrowserSourceAudioEnabled;

    /**
     * How the guests within a session should be laid out within a group browser source.
     */
    private GuestStarLayout groupLayout;

}
