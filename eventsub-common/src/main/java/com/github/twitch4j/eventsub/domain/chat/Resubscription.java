package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.enums.SubscriptionPlan;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class Resubscription {

    /**
     * The total number of months the user has subscribed.
     */
    private Integer cumulativeMonths;

    /**
     * The number of months the subscription is for.
     */
    @JsonProperty("duration_months")
    private Integer durationMonths;

    /**
     * Optional: The number of consecutive months the user has subscribed.
     */
    @Nullable
    private Integer streakMonths;

    /**
     * The type of subscription plan being used.
     * <p>
     * Does not contain {@link SubscriptionPlan#TWITCH_PRIME}; use {@link #isPrime()} instead.
     */
    private SubscriptionPlan subTier;

    /**
     * Indicates if the subscription was obtained through Amazon Prime.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_prime")
    private Boolean isPrime;

    /**
     * Whether or not the resub was a result of a gift.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_gift")
    private Boolean isGift;

    /**
     * Optional: Whether or not the gift was anonymous.
     */
    @Nullable
    @Accessors(fluent = true)
    @JsonProperty("gifter_is_anonymous")
    private Boolean isGifterAnonymous;

    /**
     * Optional: The user ID of the subscription gifter. Null if anonymous.
     */
    @Nullable
    private String gifterUserId;

    /**
     * Optional: The user name of the subscription gifter. Null if anonymous.
     */
    @Nullable
    private String gifterUserName;

    /**
     * Optional: The user login of the subscription gifter. Null if anonymous.
     */
    @Nullable
    private String gifterUserLogin;

    /**
     * @return the number of months the subscription is for.
     */
    @JsonIgnore
    public int getDurationMonths() {
        return Math.max(durationMonths, 1);
    }
}
