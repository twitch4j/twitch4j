package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.domain.GlobalCooldown;
import com.github.twitch4j.eventsub.domain.MaxPerUserPerStream;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
public class ChannelPointsReward {

	private @Nullable String id;
	private String channelId;
    private @Nullable String rewardType;
	private String title;
	private String prompt;
	private long cost;
    private @Nullable Integer defaultCost;
    private @Nullable Integer minCost;
    private @Nullable Integer defaultBitsCost;
    private @Nullable Integer bitsCost;
    private @Nullable String pricingType;
	private Boolean isUserInputRequired;
	private Boolean isSubOnly;
	private @Nullable Image image;
	private Image defaultImage;
	private @Nullable String backgroundColor;
	private @Nullable String defaultBackgroundColor;
    private @Nullable Boolean isHiddenForSubs;
	private Boolean isEnabled;
	private Boolean isPaused;
	private Boolean isInStock;
	private MaxPerStream maxPerStream;
	private Boolean shouldRedemptionsSkipRequestQueue;
	private @Nullable String updatedForIndicatorAt;
    private @Nullable Instant globallyUpdatedForIndicatorAt;
	private MaxPerUserPerStream maxPerUserPerStream;
	private GlobalCooldown globalCooldown;
	private @Nullable Integer redemptionsRedeemedCurrentStream;
	private @Nullable Instant cooldownExpiresAt;
    private @Nullable String templateId;

    @Unofficial
    public boolean isBitsRedemption() {
        return "BITS".equals(getPricingType());
    }

    @Unofficial
    public boolean isCelebration() {
        return "CELEBRATION".equals(getRewardType());
    }

    @Unofficial
    public boolean hasMessageEffect() {
        return "SEND_ANIMATED_MESSAGE".equals(getRewardType());
    }

    @Unofficial
    public boolean hasGiganticEmote() {
        return "SEND_GIGANTIFIED_EMOTE".equals(getRewardType());
    }

	@Data
	public static class Image {
		@JsonProperty("url_1x")
		private String url1x;
		@JsonProperty("url_2x")
		private String url2x;
		@JsonProperty("url_4x")
		private String url4x;
	}

	@Data
	public static class MaxPerStream {
		private Boolean isEnabled;
		private long maxPerStream;
	}
}
