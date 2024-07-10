package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class ChannelPointsRedemption {

	/**
	 * Unique ID for this redemption event
	 */
	private String id;

	/**
	 * User that requested this redemption.
	 */
	private ChannelPointsUser user;

	/**
	 * 	ID of the channel in which the reward was redeemed.
	 */
	private String channelId;

	/**
	 * Timestamp in which a reward was redeemed
	 */
	private String redeemedAt;

	/**
	 * Data about the reward that was redeemed
	 */
	private ChannelPointsReward reward;

	/**
	 * (Optional) A string that the user entered if the reward requires input
	 */
    @Nullable
	private String userInput;

	/**
	 * reward redemption status, will be FULFILLED if a user skips the reward queue, UNFULFILLED otherwise
	 */
	private String status;

    @Nullable
	@Unofficial
	private String cursor;

}
