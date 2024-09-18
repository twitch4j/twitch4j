package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.common.util.TwitchUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Indicates chat events that can be mirrored when channels are using the Stream Together Shared Chat feature.
 * <p>
 * When chats are merged, all PRIVMSG events and most USERNOTICE events are forwarded to the other channels.
 * In addition, bans/timeouts in one channel apply to all channels.
 * Thus, certain applications may wish to ignore any mirrored events while others may wish to act upon them.
 * Be wary of edge cases when your bot is joined to multiple channels in a single shared chat session.
 * You can obtain the other channels in a shared chat session via {@code TwitchHelix#getSharedChatSession}.
 *
 * @see <a href="https://dev.twitch.tv/docs/chat/irc/#shared-chat">Official Developer Explainer</a>
 */
public interface MirrorableEvent {

    /**
     * @return the raw message event
     */
    IRCMessageEvent getMessageEvent();

    /**
     * @return whether the message originated from a different source channel
     */
    default boolean isMirrored() {
        String roomId = getMessageEvent().getChannelId();
        return getSourceChannelId().filter(otherId -> !otherId.equals(roomId)).isPresent();
    }

    /**
     * @return the room id of the source channel
     */
    default Optional<String> getSourceChannelId() {
        return getMessageEvent().getTagValue("source-room-id");
    }

    /**
     * @return the id of the source message in the source channel
     */
    default Optional<String> getSourceMessageId() {
        return getMessageEvent().getTagValue("source-id");
    }

    /**
     * @return the user's chat badges in the source channel
     */
    default Optional<Map<String, String>> getSourceBadges() {
        return getMessageEvent().getTagValue("source-badges").map(TwitchUtils::parseBadges);
    }

    /**
     * @return the user's badge info in the source channel
     */
    default Optional<Map<String, String>> getSourceBadgeInfo() {
        return getMessageEvent().getTagValue("source-badge-info").map(TwitchUtils::parseBadges);
    }

    /**
     * @return the msg-id of the USERNOTICE in the source channel
     */
    default Optional<String> getSourceNoticeType() {
        return getMessageEvent().getTagValue("source-msg-id");
    }

}
