package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

/**
 * Fired when the authenticated channel to the TwitchChat instance was hosted by another user.
 * <p>
 * Not officially documented by Twitch so this could stop working at any time.
 *
 * @deprecated Twitch is removing host mode on October 3, 2022
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Unofficial
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class InboundHostEvent extends TwitchEvent {

    /**
     * Login name of the channel that was hosted (i.e., the user passed in chatAccount).
     */
    String hostTarget;

    /**
     * Display name of the user that just hosted your channel.
     */
    String hosterName;

}
