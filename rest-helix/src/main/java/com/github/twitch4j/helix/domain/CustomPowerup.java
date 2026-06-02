package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.GlobalCooldown;
import com.github.twitch4j.eventsub.domain.MaxPerStream;
import com.github.twitch4j.eventsub.domain.MaxPerUserPerStream;
import com.github.twitch4j.eventsub.domain.Reward;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomPowerup {

    /**
     * The ID that uniquely identifies the broadcaster.
     */
    private String broadcasterId;

    /**
     * The broadcaster's login name.
     */
    private String broadcasterLogin;

    /**
     * The broadcaster's display name.
     */
    private String broadcasterName;

    /**
     * The ID that uniquely identifies this custom Power-up.
     */
    private String id;

    /**
     * The title of the custom Power-up.
     */
    private String title;

    /**
     * The prompt shown to the viewer when they redeem the custom Power-up if user input is required.
     *
     * @see #isUserInputRequired()
     */
    private String prompt;

    /**
     * The amount of Bits for the custom Power-up.
     */
    private int bits;

    /**
     * A set of custom images for the custom Power-up.
     * This field is null if the broadcaster didn't upload images.
     */
    @Nullable
    private Reward.Image image;

    /**
     * A set of default images for the custom Power-up.
     */
    private Reward.Image defaultImage;

    /**
     * The background color to use for the custom Power-up.
     */
    private String backgroundColor;

    /**
     * Indicates whether the custom Power-up is enabled.
     */
    @JsonProperty("is_enabled")
    private boolean isEnabled;

    /**
     * Indicates whether the user must enter information when redeeming the custom Power-up.
     */
    @JsonProperty("is_user_input_required")
    private boolean isUserInputRequired;

    /**
     * The settings used to determine whether to apply a maximum to the number of redemptions allowed per live stream.
     */
    private MaxPerStream maxPerStreamSetting;

    /**
     * The settings used to determine whether to apply a maximum to the number of redemptions allowed per user per live stream.
     */
    private MaxPerUserPerStream maxPerUserPerStreamSetting;

    /**
     * The settings used to determine whether to apply a cooldown period between redemptions and the length of the cooldown.
     */
    private GlobalCooldown globalCooldownSetting;

    /**
     * Indicates whether the custom Power-up is currently paused.
     * Viewers can’t redeem paused custom Power-ups.
     */
    @JsonProperty("is_paused")
    private boolean isPaused;

    /**
     * Indicates whether the custom Power-up is currently in stock.
     * Viewers can’t redeem out of stock custom Power-ups.
     */
    @JsonProperty("is_in_stock")
    private boolean isInStock;

    /**
     * The number of redemptions redeemed during the current live stream.
     * The number counts against the max_per_stream_setting limit.
     * This field is null if the broadcaster’s stream isn’t live or max_per_stream_setting isn’t enabled.
     *
     * @see #getMaxPerStreamSetting()
     */
    @Nullable
    private Integer redemptionsRedeemedCurrentStream;

    /**
     * The timestamp of when the cooldown period expires.
     * Is null if the custom Power-up isn’t in a cooldown state.
     *
     * @see #getGlobalCooldownSetting()
     */
    @Nullable
    private Instant cooldownExpiresAt;

}
