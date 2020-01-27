package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Calendar;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RewardRedeemedEvent extends ChannelPointsRedemptionEvent {

	public RewardRedeemedEvent(Calendar timestamp, ChannelPointsRedemption redemption) {
		super(timestamp, redemption);
	}

}
