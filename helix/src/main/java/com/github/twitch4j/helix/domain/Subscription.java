package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Subscription {

    /** User ID of the broadcaster. */
    @NonNull
    private String broadcasterId;

    /** Login name of the broadcaster. */
    @NonNull
    private String broadcasterLogin;

    /** Display name of the broadcaster. */
    @NonNull
    private String broadcasterName;

    /** Determines if the subscription is a gift subscription. */
    private Boolean isGift;

    /** ID of the user who gifted the sub. */
    private String gifterId;

    /** If the subscription was gifted, this is the login of the gifter. */
    private String gifterLogin;

    /** Display name of the user who gifted the sub. */
    private String gifterName;

    /** Type of subscription (Tier 1, Tier 2, Tier 3). 1000 = Tier 1, 2000 = Tier 2, 3000 = Tier 3 subscriptions. */
    @NonNull
    private String tier;

    /** Name of the subscription. */
    @NonNull
    private String planName;

    /** ID of the subscribed user. */
    @NonNull
    private String userId;

    /** Login of the subscribed user. */
    @NonNull
    private String userLogin;

    /** Display name of the subscribed user. */
    @NonNull
    private String userName;

    /**
     * @return the subscription plan name
     * @deprecated will be removed in favor of .getPlanName()
     */
    @Deprecated
    @JsonIgnore
    public String getPlan_name() {
        return this.planName;
    }

}
