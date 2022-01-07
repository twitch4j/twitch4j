package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.Reward;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomReward {

    /**
     * ID of the channel the reward is for.
     */
    private String broadcasterId;

    /**
     * Login of the channel the reward is for.
     */
    private String broadcasterLogin;

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
    private Reward.Image image;

    /**
     * Set of default images of 1x, 2x and 4x sizes for the reward
     */
    private Reward.Image defaultImage;

    /**
     * Whether the reward currently enabled; if false the reward won’t show up to viewers.
     * Default: true.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    /**
     * Custom background color for the reward.
     * <p>
     * Format: Hex with {@literal #} prefix, ideally in ALL CAPS.
     */
    private String backgroundColor;

    /**
     * Does the user need to enter information when redeeming the reward.
     * Default: false.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_user_input_required")
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
    @Accessors(fluent = true)
    @JsonProperty("is_paused")
    private Boolean isPaused;

    /**
     * Whether the reward is currently in stock; if false viewers can’t redeem.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_in_stock")
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
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @SuperBuilder(toBuilder = true)
    @Jacksonized
    @EqualsAndHashCode(callSuper = false)
    public static class MaxPerStreamSetting extends Setting {

        @Getter(onMethod_ = { @JsonIgnore })
        @Accessors(fluent = true)
        @JsonAlias("is_enabled")
        @JsonProperty("is_max_per_stream_enabled")
        private Boolean isEnabled;

        private Integer maxPerStream;

    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @SuperBuilder(toBuilder = true)
    @Jacksonized
    @EqualsAndHashCode(callSuper = false)
    public static class MaxPerUserPerStreamSetting extends Setting {

        @Getter(onMethod_ = { @JsonIgnore })
        @Accessors(fluent = true)
        @JsonAlias("is_enabled")
        @JsonProperty("is_max_per_user_per_stream_enabled")
        private Boolean isEnabled;

        private Integer maxPerUserPerStream;

    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @SuperBuilder(toBuilder = true)
    @Jacksonized
    @EqualsAndHashCode(callSuper = false)
    public static class GlobalCooldownSetting extends Setting {

        @Getter(onMethod_ = { @JsonIgnore })
        @Accessors(fluent = true)
        @JsonAlias("is_enabled")
        @JsonProperty("is_global_cooldown_enabled")
        private Boolean isEnabled;

        private Integer globalCooldownSeconds;

    }

    @NoArgsConstructor
    @SuperBuilder(toBuilder = true)
    public static abstract class Setting {
        abstract Boolean isEnabled();
    }

}
