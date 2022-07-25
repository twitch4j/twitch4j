package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * The type of goal.
 */
public enum GoalType {

    /**
     * The goal is to increase followers.
     */
    @JsonAlias("follower")
    FOLLOWERS,

    /**
     * The goal is to increase subscriptions.
     * This type shows the net increase or decrease in subscriptions.
     */
    @JsonAlias("subscription")
    SUBSCRIPTIONS,

    /**
     * The goal is to increase subscriptions.
     * This type shows only the net increase in subscriptions (it does not account for users that stopped subscribing since the goal's inception).
     */
    @JsonAlias("new_subscription")
    NEW_SUBSCRIPTIONS,

    /**
     * The goal is to increase subscriptions.
     * This type shows the net increase or decrease in the number of subscriptions.
     */
    @JsonAlias("subscription_count")
    SUB_COUNT,

    /**
     * The goal is to increase subscriptions.
     * This type shows only the net increase in the number of subscriptions (it does not account for users that unsubscribed since the goal started).
     */
    @JsonAlias("new_subscription_count")
    NEW_SUB_COUNT

}
