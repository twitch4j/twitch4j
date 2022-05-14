package com.github.twitch4j.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.domain.Clip;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Optional;

/**
 * Called when a new clip is created in a channel.
 * <p>
 * Fired by {@link com.github.twitch4j.TwitchClientHelper}; so this event must explicitly be enabled for specific channels.
 * <p>
 * Due to Twitch heavily caching the get clips endpoint, these creation events can have multi-minute delays.
 *
 * @see com.github.twitch4j.IClientHelper#enableClipEventListener(String, String)
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelClipCreatedEvent extends TwitchEvent {

    /**
     * The channel where the clip was created.
     */
    EventChannel channel;

    /**
     * The clip that was created.
     */
    Clip clip;

    /**
     * @return the user that created the clip
     */
    public Optional<EventUser> getCreatingUser() {
        return Optional.ofNullable(clip.getCreatorId()).map(id -> new EventUser(id, clip.getCreatorName()));
    }

}
