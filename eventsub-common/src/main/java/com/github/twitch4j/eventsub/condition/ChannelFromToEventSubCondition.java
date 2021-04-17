package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Generic condition when a broadcaster can be either on the receiving or giving end of the event type.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
public class ChannelFromToEventSubCondition extends EventSubCondition {

    /**
     * The broadcaster user ID that triggered the channel event you want to get notifications for.
     */
    private String fromBroadcasterUserId;

    /**
     * The broadcaster user ID that received the channel event you want to get notifications for.
     */
    private String toBroadcasterUserId;

}
