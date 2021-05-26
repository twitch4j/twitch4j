package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * Fired when the authenticated channel to the TwitchChat instance was hosted by another user.
 * <p>
 * Not officially documented by Twitch so this could stop working at any time.
 */
@Value
@EqualsAndHashCode(callSuper = true)
@Unofficial
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
