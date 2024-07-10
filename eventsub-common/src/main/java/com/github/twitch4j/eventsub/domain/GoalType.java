package com.github.twitch4j.eventsub.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

/**
 * The type of goal.
 */
public enum GoalType {

    /**
     * The goal is to increase followers.
     */
    @JsonAlias({ "follower", "follow" })
    FOLLOWERS,

    /**
     * The goal is to increase subscriptions.
     * This type shows the net increase or decrease in tier points associated with the subscriptions.
     */
    @JsonAlias({ "subscription", "sub" })
    SUBSCRIPTIONS,

    /**
     * The goal is to increase subscriptions.
     * This type shows only the net increase in tier points associated with the subscriptions (it does not account for users that unsubscribed since the goal started).
     */
    @JsonAlias({ "new_subscription", "new_sub" })
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
    NEW_SUB_COUNT,

    /**
     * The goal is to increase the amount of Bits used on the channel.
     *
     * @see <a href="https://twitter.com/TwitchSupport/status/1778470287564058836">Twitch Announcement</a>
     */
    @JsonAlias("new_bit")
    BITS,

    /**
     * The goal is to increase the number of unique Cheerers to Cheer on the channel.
     *
     * @see <a href="https://twitter.com/TwitchSupport/status/1778470287564058836">Twitch Announcement</a>
     */
    @JsonAlias("new_cheerer")
    CHEERS,

    /**
     * An unknown goal type; please create a GitHub issue.
     */
    @JsonEnumDefaultValue
    UNKNOWN

}
