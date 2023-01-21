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
public final class ShoutoutCreateEvent extends EventSubChannelFromToEvent implements ModeratorEvent {

    /**
     * An ID that identifies the moderator that sent the Shoutout.
     * If the broadcaster sent the Shoutout, this ID is the same as the ID in broadcaster_user_id.
     */
    private final String moderatorUserId;

    /**
     * The moderator’s display name.
     */
    private final String moderatorUserName;

    /**
     * The moderator’s login name.
     */
    private final String moderatorUserLogin;

    /**
     * The number of users that were watching the broadcaster’s stream at the time of the Shoutout.
     */
    private final Integer viewerCount;

    /**
     * The UTC timestamp (in RFC3339 format) of when the broadcaster may send a Shoutout to a different broadcaster.
     */
    private final Instant cooldownEndsAt;
    /**
     * The UTC timestamp (in RFC3339 format) of when the broadcaster may send another Shoutout to the broadcaster in to_broadcaster_user_id.
     */
    private final Instant targetCooldownEndsAt;

    /**
     * The UTC timestamp of when the moderator sent the Shoutout.
     */
    private final Instant startedAt;

    @JsonCreator
    public ShoutoutCreateEvent(
        @JsonProperty("broadcaster_user_id") @JsonAlias("from_broadcaster_user_id") String fromId,
        @JsonProperty("broadcaster_user_name") @JsonAlias("from_broadcaster_user_name") String fromName,
        @JsonProperty("broadcaster_user_login") @JsonAlias("from_broadcaster_user_login") String fromLogin,
        @JsonProperty("moderator_user_id") String modId,
        @JsonProperty("moderator_user_name") String modName,
        @JsonProperty("moderator_user_login") String modLogin,
        @JsonProperty("to_broadcaster_user_id") String toId,
        @JsonProperty("to_broadcaster_user_name") String toName,
        @JsonProperty("to_broadcaster_user_login") String toLogin,
        @JsonProperty("started_at") Instant startedAt,
        @JsonProperty("viewer_count") Integer viewers,
        @JsonProperty("cooldown_ends_at") Instant cooldownEndsAt,
        @JsonProperty("target_cooldown_ends_at") Instant targetCooldownEndsAt
    ) {
        super(fromId, fromLogin, fromName, toId, toLogin, toName);
        this.moderatorUserId = modId;
        this.moderatorUserName = modName;
        this.moderatorUserLogin = modLogin;
        this.startedAt = startedAt;
        this.viewerCount = viewers;
        this.cooldownEndsAt = cooldownEndsAt;
        this.targetCooldownEndsAt = targetCooldownEndsAt;
    }
}
