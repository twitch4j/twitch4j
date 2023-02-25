package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * Generic condition when a broadcaster can be either on the receiving or giving end of the event type.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChannelFromToEventSubCondition)) return false;

        ChannelFromToEventSubCondition that = (ChannelFromToEventSubCondition) o;
        return defaultString(fromBroadcasterUserId).equals(defaultString(that.fromBroadcasterUserId))
            && defaultString(toBroadcasterUserId).equals(defaultString(that.toBroadcasterUserId));
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultString(fromBroadcasterUserId), defaultString(toBroadcasterUserId));
    }
}
