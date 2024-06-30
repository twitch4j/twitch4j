package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.GlobalCooldown;
import com.github.twitch4j.eventsub.domain.MaxPerUserPerStream;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(onMethod_ = { @Deprecated })
public class ChannelPointsReward {

	private String id;
	private String channelId;
	private String title;
	private String prompt;
	private long cost;
	private Boolean isUserInputRequired;
	private Boolean isSubOnly;
	private @Nullable Image image;
	private Image defaultImage;
	private String backgroundColor;
	private Boolean isEnabled;
	private Boolean isPaused;
	private Boolean isInStock;
	private MaxPerStream maxPerStream;
	private Boolean shouldRedemptionsSkipRequestQueue;
	private String updatedForIndicatorAt;
	private MaxPerUserPerStream maxPerUserPerStream;
	private GlobalCooldown globalCooldown;
	private @Nullable Integer redemptionsRedeemedCurrentStream;
	private @Nullable Instant cooldownExpiresAt;
    private @Nullable String templateId;

	@Data
    @Setter(onMethod_ = { @Deprecated })
	public static class Image {
		@JsonProperty("url_1x")
		private String url1x;
		@JsonProperty("url_2x")
		private String url2x;
		@JsonProperty("url_4x")
		private String url4x;
	}

	@Data
    @Setter(onMethod_ = { @Deprecated })
	public static class MaxPerStream {
		private Boolean isEnabled;
		private long maxPerStream;
	}
}
