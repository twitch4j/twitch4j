package com.github.twitch4j.chat.events.channel;

import lombok.Value;

/**
 * A chat instance failed to join (or stay joined to) a channel AND removeChannelOnJoinFailure was enabled,
 * resulting in removal from the instance's current channels.
 */
@Value
public class ChannelJoinFailureEvent {

    /**
     * The name of the channel that the chat instance was unable to join.
     */
    String channelName;

    /**
     * The inferred reason for the join failure.
     */
    Reason reason;

    // Note: NoticeTag.MSG_ROOM_NOT_FOUND is (unfortunately) never sent by twitch, so haven't bothered including logic for it
    public enum Reason {

        /**
         * The chat instance was unable to join the channel, exhausting maxJoinRetries.
         * Since removeChannelOnJoinFailure was enabled, the channel was removed.
         * <p>
         * This can occur when the channel simply does not exist or the user was already banned from the channel.
         */
        RETRIES_EXHAUSTED,

        /**
         * The chat user was banned from an initially successfully joined channel.
         * <p>
         * Twitch does not send this state upon initially joining a channel, if the user is already banned.
         */
        USER_BANNED,

        /**
         * The channel was suspended, so further chat messages cannot be received.
         * <p>
         * This can be sent upon initial join (if the channel was already suspended) or even after a successful initial join (the channel was not initially suspended).
         */
        CHANNEL_SUSPENDED

    }

}
