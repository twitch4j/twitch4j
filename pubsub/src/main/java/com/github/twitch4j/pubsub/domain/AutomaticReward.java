package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.eventsub.domain.GlobalCooldown;
import com.github.twitch4j.eventsub.domain.MaxPerUserPerStream;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@Unofficial
public class AutomaticReward {

    private String channelId;
    private RewardType rewardType;
    private int cost;
    private int defaultCost;
    private int minCost;
    private int bitsCost;
    private int defaultBitsCost;
    private String pricingType;
    private @Nullable ChannelPointsReward.Image image;
    private ChannelPointsReward.Image defaultImage;
    private @Nullable String backgroundColor;
    private String defaultBackgroundColor;
    private @Nullable Instant updatedForIndicatorAt;
    private Instant globallyUpdatedForIndicatorAt;
    private ChannelPointsReward.MaxPerStream maxPerStream;
    private MaxPerUserPerStream maxPerUserPerStream;
    private GlobalCooldown globalCooldown;
    private @Nullable Instant cooldownExpiresAt;
    private @Nullable Integer redemptionsRedeemedCurrentStream;

    @Accessors(fluent = true)
    @JsonProperty("is_enabled")
    private Boolean isEnabled;

    @Accessors(fluent = true)
    @JsonProperty("is_hidden_for_subs")
    private Boolean isHiddenForSubs;

    @Accessors(fluent = true)
    @JsonProperty("is_in_stock")
    private Boolean isInStock;

    @Unofficial
    public boolean isBitsRedemption() {
        return "BITS".equals(getPricingType());
    }

    public enum RewardType {
        CELEBRATION,
        SEND_ANIMATED_MESSAGE,
        SEND_GIGANTIFIED_EMOTE,
        @JsonEnumDefaultValue
        UNKNOWN
    }

}
