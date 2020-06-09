package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelPointsReward {

	private String id;
	private String channelId;
	private String title;
	private String prompt;
	private long cost;
	private Boolean isUserInputRequired;
	private Boolean isSubOnly;
	private Image image;
	private Image defaultImage;
	private String backgroundColor;
	private Boolean isEnabled;
	private Boolean isPaused;
	private Boolean isInStock;
	private MaxPerStream maxPerStream;
	private Boolean shouldRedemptionsSkipRequestQueue;
	private String updatedForIndicatorAt;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Image {
		@JsonProperty("url_1x")
		private String url1x;
		@JsonProperty("url_2x")
		private String url2x;
		@JsonProperty("url_4x")
		private String url4x;
	}

	@Data
	@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MaxPerStream {
		private Boolean isEnabled;
		private long maxPerStream;
	}
}
