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
     */
    @JsonAlias("subscription")
    SUBSCRIPTIONS

}
