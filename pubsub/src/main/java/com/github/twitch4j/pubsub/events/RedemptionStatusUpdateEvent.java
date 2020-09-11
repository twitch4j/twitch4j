package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RedemptionStatusUpdateEvent extends ChannelPointsRedemptionEvent {

    public RedemptionStatusUpdateEvent(Instant timestamp, ChannelPointsRedemption redemption) {
        super(timestamp, redemption);
    }

}
