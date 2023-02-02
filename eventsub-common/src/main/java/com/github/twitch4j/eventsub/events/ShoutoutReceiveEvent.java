package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class ShoutoutReceiveEvent extends EventSubChannelFromToEvent {

    /**
     * The number of users that were watching the from-broadcaster’s stream at the time of the Shoutout.
     */
    private final Integer viewerCount;

    /**
     * The UTC timestamp of when the moderator sent the Shoutout.
     */
    private final Instant startedAt;

    @JsonCreator
    public ShoutoutReceiveEvent(
        @JsonProperty("broadcaster_user_id") @JsonAlias("to_broadcaster_user_id") String toId,
        @JsonProperty("broadcaster_user_name") @JsonAlias("to_broadcaster_user_name") String toName,
        @JsonProperty("broadcaster_user_login") @JsonAlias("to_broadcaster_user_login") String toLogin,
        @JsonProperty("from_broadcaster_user_id") String fromId,
        @JsonProperty("from_broadcaster_user_name") String fromName,
        @JsonProperty("from_broadcaster_user_login") String fromLogin,
        @JsonProperty("viewer_count") Integer viewers,
        @JsonProperty("started_at") Instant startedAt
    ) {
        super(fromId, fromLogin, fromName, toId, toLogin, toName);
        this.viewerCount = viewers;
        this.startedAt = startedAt;
    }
}
