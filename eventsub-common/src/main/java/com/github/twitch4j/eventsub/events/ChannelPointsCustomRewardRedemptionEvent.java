package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.twitch4j.helix.domain.CustomRewardRedemption;
import com.github.twitch4j.eventsub.domain.Reward;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ChannelPointsCustomRewardRedemptionEvent extends EventSubUserChannelEvent {

    /**
     * The redemption identifier.
     */
    private String id;

    /**
     * The user input provided. Empty string if not provided.
     */
    private String userInput;

    /**
     * Defaults to unfulfilled. Possible values are unknown, unfulfilled, fulfilled, and canceled.
     */
    @JsonIgnore
    private CustomRewardRedemption.Status status;

    /**
     * Basic information about the reward that was redeemed, at the time it was redeemed.
     */
    private Reward reward;

    /**
     * RFC3339 timestamp of when the reward was redeemed.
     */
    private Instant redeemedAt;

    @JsonProperty("status")
    private void unpackStatus(String status) {
        try {
            this.status = CustomRewardRedemption.Status.valueOf(status.toUpperCase());
        } catch (Exception e) {
            this.status = CustomRewardRedemption.Status.UNKNOWN;
        }
    }

}
