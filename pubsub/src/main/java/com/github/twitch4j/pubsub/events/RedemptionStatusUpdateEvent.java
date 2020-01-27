package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Calendar;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RedemptionStatusUpdateEvent extends ChannelPointsRedemptionEvent {

	public RedemptionStatusUpdateEvent(Calendar timestamp, ChannelPointsRedemption redemption) {
		super(timestamp, redemption);
	}

}
