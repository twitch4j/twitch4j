package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = true)
public class ChannelPointsRedemptionEvent extends TwitchEvent {

    /**
     * The time the pubsub message was sent
     */
    private final Calendar timestamp;

    /**
     * Data about the redemption, includes unique id and user that redeemed it
     */
    private final ChannelPointsRedemption redemption;

}
