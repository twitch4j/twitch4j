package com.github.twitch4j.chat.events;

import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;

/**
 * This event is a base for events that originate from a channel
 */
@Data
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AbstractChannelEvent extends TwitchEvent {

    /**
     * Event Channel
     */
    private EventChannel channel;

    /**
     * Event Constructor
     *
     * @param channel The channel that this event originates from.
     */
    public AbstractChannelEvent(EventChannel channel) {
        super();
        this.channel = channel;
    }

    /**
     * Timeout a user
     *
     * @param user     username
     * @param duration duration
     * @param reason   reason
     * @deprecated Twitch decommissioned this method on February 18, 2023; migrate to TwitchHelix#banUser
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public void timeout(String user, Duration duration, String reason) {
        StringBuilder sb = new StringBuilder()
            .append(duration.getSeconds());
        if (reason != null) {
            sb.append(" ").append(reason);
        }
        getTwitchChat().sendMessage(channel.getName(), String.format("/timeout %s %s", user, sb.toString()));
    }

    /**
     * Ban a user
     *
     * @param user   username
     * @param reason reason
     * @deprecated Twitch decommissioned this method on February 18, 2023; migrate to TwitchHelix#banUser
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public void ban(String user, String reason) {
        StringBuilder sb = new StringBuilder(user);
        if (reason != null) {
            sb.append(" ").append(reason);
        }

        getTwitchChat().sendMessage(channel.getName(), String.format("/ban %s", sb.toString()));
    }

    /**
     * Unban a user
     *
     * @param user username
     * @deprecated Twitch decommissioned this method on February 18, 2023; migrate to TwitchHelix#unbanUser
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public void unban(String user) {
        getTwitchChat().sendMessage(channel.getName(), String.format("/unban %s", user));
    }

}
