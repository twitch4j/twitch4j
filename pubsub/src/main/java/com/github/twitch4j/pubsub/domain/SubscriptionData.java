package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionData {

    /**
     * The login name for the user that purchased the subscription
     */
    private String userName;

    /**
     * The display name for the user that purchased the subscription
     */
    private String displayName;

    /**
     * The name of the channel where the subscription took place
     */
    private String channelName;

    /**
     * The id for the user that purchased the subscription
     */
    private String userId;

    /**
     * The id of the channel where the subscription took place
     */
    private String channelId;

    /**
     * RFC 3339 timestamp of when the subscription took place
     */
    private String time;

    /**
     * The type of subscription plan that was purchased;
     * "Prime" or "1000" or "2000" or "3000"
     */
    private String subPlan;

    /**
     * The name of the subscription plan that was purchased
     */
    private String subPlanName;

    /**
     * The number of months the receiving user been subscribed
     *
     * @see SubscriptionData#getCumulativeMonths()
     * @see SubscriptionData#getStreakMonths()
     */
    @Deprecated
    private Integer months;

    /**
     * How many months in total has the receiving user been subscribed
     */
    private Integer cumulativeMonths;

    /**
     * How many months in a row has the receiving user been subscribed
     */
    private Integer streakMonths;

    /**
     * The event type for this subscription;
     * "sub" or "resub" or "subgift" or "anonsubgift"
     */
    private String context;

    /**
     * The accompanying message when the subscription was shared
     */
    private CommerceMessage subMessage;

    /**
     * The id of the user that received the subscription
     */
    private String recipientId = this.userId;

    /**
     * The login name of the user that received the subscription
     */
    private String recipientUserName = this.userName;

    /**
     * The display name of the user that received the subscription
     */
    private String recipientDisplayName = this.displayName;

}
