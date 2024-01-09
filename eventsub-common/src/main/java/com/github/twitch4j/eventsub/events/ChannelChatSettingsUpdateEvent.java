package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelChatSettingsUpdateEvent extends EventSubChannelEvent {

    /**
     * Whether chat messages must contain only emotes.
     */
    @Accessors(fluent = true)
    @JsonProperty("emote_mode")
    private Boolean isEmoteMode;

    /**
     * Whether the broadcaster restricts the chat room to followers only, based on how long they’ve followed.
     * <p>
     * See {@link #getFollowerModeDurationMinutes()} for how long the followers must have followed the broadcaster to participate in the chat room.
     */
    @Accessors(fluent = true)
    @JsonProperty("follower_mode")
    private Boolean isFollowerMode;

    /**
     * The length of time, in minutes, that the followers must have followed the broadcaster to participate in the chat room.
     * <p>
     * Null if {@link #isFollowerMode()} is not true.
     */
    @Nullable
    private Integer followerModeDurationMinutes;

    /**
     * Whether the broadcaster limits how often users in the chat room are allowed to send messages.
     * <p>
     * See {@link #getSlowModeWaitTimeSeconds()} for the delay.
     */
    @Accessors(fluent = true)
    @JsonProperty("slow_mode")
    private Boolean isSlowMode;

    /**
     * The amount of time, in seconds, that users need to wait between sending messages.
     * <p>
     * Null if {@link #isSlowMode()} is not true.
     */
    @Nullable
    private Integer slowModeWaitTimeSeconds;

    /**
     * Whether only users that subscribe to the broadcaster’s channel can talk in the chat room.
     */
    @Accessors(fluent = true)
    @JsonProperty("subscriber_mode")
    private Boolean isSubscriberMode;

    /**
     * Whether the broadcaster requires users to post only unique messages in the chat room.
     */
    @Accessors(fluent = true)
    @JsonProperty("unique_chat_mode")
    private Boolean isUniqueMode;

}
