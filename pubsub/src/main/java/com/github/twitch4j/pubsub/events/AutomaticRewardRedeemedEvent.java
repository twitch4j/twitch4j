package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.AutomaticRewardRedemption;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.Instant;

/**
 * Fired when certain automatic rewards are redeemed.
 * In particular, celebrations, message effects, and gigantified emotes (i.e., new bits power-ups) trigger this event.
 * This event is <i>not</i> fired for message highlights or subs-only bypass messages or emote unlocks.
 */
@Value
@Unofficial
@EqualsAndHashCode(callSuper = false)
public class AutomaticRewardRedeemedEvent extends TwitchEvent {

    /**
     * The time when the reward was redeemed.
     */
    Instant timestamp;

    /**
     * Data about the redemption.
     */
    AutomaticRewardRedemption redemption;

}
