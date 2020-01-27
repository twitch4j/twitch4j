package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
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
	private String userInput;

	/**
	 * reward redemption status, will be FULFULLED if a user skips the reward queue, UNFULFILLED otherwise
	 */
	private String status;

}
