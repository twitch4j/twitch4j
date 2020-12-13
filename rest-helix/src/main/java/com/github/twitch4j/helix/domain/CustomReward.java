package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@With
@Builder(toBuilder = true)
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomReward {

    /**
     * ID of the channel the reward is for.
     */
    private String broadcasterId;

    /**
     * Display name of the channel the reward is for.
     */
    private String broadcasterName;

    /**
     * ID of the reward.
     */
    private String id;

    /**
     * The title of the reward.
     */
    private String title;

    /**
     * The prompt for the viewer when they are redeeming the reward.
     */
    private String prompt;

    /**
     * The cost of the reward.
     */
    private Integer cost;

    /**
     * Set of custom images of 1x, 2x and 4x sizes for the reward.
     * Can be null if no images have been uploaded.
     */
    @Nullable
    private Image image;

    /**
     * Set of default images of 1x, 2x and 4x sizes for the reward
     */
    private Image defaultImage;

    /**
     * Whether the reward currently enabled; if false the reward won’t show up to viewers.
     * Default: true.
     */
    private Boolean isEnabled;

    /**
     * Custom background color for the reward.
     * <p>
     * Format: Hex with {@literal #} prefix.
     */
    private String backgroundColor;

    /**
     * Does the user need to enter information when redeeming the reward.
     * Default: false.
     */
    private Boolean isUserInputRequired;

    /**
     * Whether a maximum per stream is enabled and what the maximum is.
     */
    private MaxPerStreamSetting maxPerStreamSetting;

    /**
     * Whether a maximum per user per stream is enabled and what the maximum is.
     */
    private MaxPerUserPerStreamSetting maxPerUserPerStreamSetting;

    /**
     * Whether a cooldown is enabled and what the cooldown is.
     */
    private GlobalCooldownSetting globalCooldownSetting;

    /**
     * Whether the reward is currently paused; if true viewers can’t redeem.
     */
    private Boolean isPaused;

    /**
     * Whether the reward is currently in stock; if false viewers can’t redeem.
     */
    private Boolean isInStock;

    /**
     * Whether redemptions should be set to FULFILLED status immediately when redeemed and skip the request queue instead of the normal UNFULFILLED status.
     */
    private Boolean shouldRedemptionsSkipRequestQueue;

    /**
     * The number of redemptions redeemed during the current live stream.
     * Counts against the max_per_stream_setting limit.
     * Null if the broadcasters stream isn’t live or max_per_stream_setting isn’t enabled.
     */
    @Nullable
    private Integer redemptionsRedeemedCurrentStream;

    /**
     * Timestamp of the cooldown expiration.
     * Null if the reward isn’t on cooldown.
     */
    @Nullable
    private Instant cooldownExpiresAt;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
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
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MaxPerStreamSetting extends Setting {
        private Integer maxPerStream;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MaxPerUserPerStreamSetting extends Setting {
        private Integer maxPerUserPerStream;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GlobalCooldownSetting extends Setting {
        private Integer globalCooldownSeconds;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Setting {
        private Boolean isEnabled;
    }

}
