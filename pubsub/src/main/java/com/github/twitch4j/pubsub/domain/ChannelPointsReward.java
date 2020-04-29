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
	private boolean userInputRequired;
	private boolean subOnly;
	private Image image;
	private Image defaultImage;
	private String backgroundColor;
	private boolean enabled;
	private boolean paused;
	private boolean inStock;
	private MaxPerStream maxPerStream;
	private boolean shouldRedemptionsSkipRequestQueue;

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
		private boolean isEnabled;
		private long maxPerStream;
	}
}
