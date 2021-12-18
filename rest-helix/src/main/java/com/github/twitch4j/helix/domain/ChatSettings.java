package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatSettings {

    /**
     * The ID of the broadcaster specified in the request.
     */
    private String broadcasterId;

    /**
     * A Boolean value that determines whether chat messages must contain only emotes.
     * Is true, if only messages that are 100% emotes are allowed; otherwise, false.
     */
    @Accessors(fluent = true)
    @JsonProperty("emote_mode")
    private Boolean isEmoteOnlyMode;

    /**
     * A Boolean value that determines whether the broadcaster restricts the chat room to followers only, based on how long they’ve followed.
     * Is true, if the broadcaster restricts the chat room to followers only; otherwise, false.
     *
     * @see #getFollowerModeDuration()
     */
    @Accessors(fluent = true)
    @JsonProperty("follower_mode")
    private Boolean isFollowersOnlyMode;

    /**
     * The length of time, in minutes, that the followers must have followed the broadcaster to participate in the chat room
     * Is null if follower_mode is false.
     * <p>
     * When setting this through the API, the possible values range from 0 (all followers) to 129600 (3 months). The default is 0.
     *
     * @see #isFollowersOnlyMode()
     */
    @Nullable
    private Integer followerModeDuration;

    /**
     * The moderator’s ID.
     * The response includes this field only if the request specifies a User access token that includes the moderator:read:chat_settings scope.
     *
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_SETTINGS_READ
     */
    @Nullable
    private String moderatorId;

    /**
     * A Boolean value that determines whether the broadcaster adds a short delay before chat messages appear in the chat room.
     * This gives chat moderators and bots a chance to remove them before viewers can see the message.
     * <p>
     * Is true, if the broadcaster applies a delay; otherwise, false.
     * <p>
     * The response includes this field only if the request specifies a User access token that includes the moderator:read:chat_settings scope.
     *
     * @see #getNonModeratorChatDelayDuration()
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_SETTINGS_READ
     */
    @Nullable
    @Accessors(fluent = true)
    @JsonProperty("non_moderator_chat_delay")
    private Boolean hasNonModeratorChatDelay;

    /**
     * The amount of time, in seconds, that messages are delayed from appearing in chat.
     * <p>
     * Is null if non_moderator_chat_delay is false.
     * <p>
     * The response includes this field only if the request specifies a User access token that includes the moderator:read:chat_settings scope.
     * <p>
     * When setting this through the API, the possible values are:
     * <ul>
     *     <li>2 — 2 second delay (recommended)</li>
     *     <li>4 — 4 second delay</li>
     *     <li>6 — 6 second delay</li>
     * </ul>
     *
     * @see #hasNonModeratorChatDelay()
     * @see com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHAT_SETTINGS_READ
     */
    @Nullable
    private Integer nonModeratorChatDelayDuration;

    /**
     * A Boolean value that determines whether the broadcaster limits how often users in the chat room are allowed to send messages.
     * <p>
     * Is true, if the broadcaster applies a delay; otherwise, false.
     *
     * @see #getSlowModeWaitTime()
     */
    @Accessors(fluent = true)
    @JsonProperty("slow_mode")
    private Boolean isSlowMode;

    /**
     * The amount of time, in seconds, that users need to wait between sending messages.
     * <p>
     * Is null if slow_mode is false.
     * <p>
     * When setting this through the API, the possible values range from 3 (three-second delay) to 120 (two-minute delay). The default is 30 seconds.
     *
     * @see #isSlowMode()
     */
    @Nullable
    private Integer slowModeWaitTime;

    /**
     * A Boolean value that determines whether only users that subscribe to the broadcaster’s channel can talk in the chat room.
     * <p>
     * Is true, if the broadcaster restricts the chat room to subscribers only; otherwise, false.
     */
    @Accessors(fluent = true)
    @JsonProperty("subscriber_mode")
    private Boolean isSubscribersOnlyMode;

    /**
     * A Boolean value that determines whether the broadcaster requires users to post only unique messages in the chat room.
     * <p>
     * Is true, if the broadcaster requires unique messages only; otherwise, false.
     */
    @Accessors(fluent = true)
    @JsonProperty("unique_chat_mode")
    private Boolean isUniqueChatMode;

    /**
     * @return the {@link Duration} of the followers-only period, or null if not enabled.
     */
    @Nullable
    public Duration getFollowersOnlyLength() {
        return followerModeDuration != null ? Duration.of(followerModeDuration, ChronoUnit.MINUTES) : null;
    }

    /**
     * @return the {@link Duration} of the non-moderator chat delay, or null if not present.
     */
    @Nullable
    public Duration getNonModChatDelayLength() {
        return nonModeratorChatDelayDuration != null ? Duration.of(nonModeratorChatDelayDuration, ChronoUnit.SECONDS) : null;
    }

    /**
     * @return the {@link Duration} of the slow mode, or null if not enabled.
     */
    @Nullable
    public Duration getSlowModeLength() {
        return slowModeWaitTime != null ? Duration.of(slowModeWaitTime, ChronoUnit.SECONDS) : null;
    }

}
