package com.github.twitch4j.chat.util;

import com.github.twitch4j.chat.ITwitchChat;
import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.util.CryptoUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.twitch4j.chat.events.channel.IRCMessageEvent.NONCE_TAG_NAME;

/**
 * Information regarding a Crowd Chant participation or initiation.
 *
 * @see <a href="https://twitch.uservoice.com/forums/310201-chat/suggestions/43451310--test-crowd-chant">Related Uservoice</a>
 * @deprecated <a href="https://twitter.com/TwitchSupport/status/1486036628523073539">Will be disabled on 2022-02-02</a>
 */
@Value
@Unofficial
@Deprecated
public class ChatCrowdChant {

    public static final String CHANT_MSG_ID_TAG_NAME = "crowd-chant-parent-msg-id";

    /**
     * The id for the parent message in the Crowd Chant.
     */
    String messageId;

    /**
     * The text being chanted.
     */
    String text;

    /**
     * Whether this message initiated a crowd chat.
     * When false, the message is simply participating in the chant, rather than initiating.
     */
    boolean initiator;

    /**
     * The name of the channel where the Crowd Chant took place.
     */
    @Getter(AccessLevel.PRIVATE)
    String channelName;

    /**
     * Sends the same message in the same channel to participate in the Crowd Chant, with the proper chat tag.
     *
     * @param chat an authenticated TwitchChat instance.
     * @deprecated <a href="https://twitter.com/TwitchSupport/status/1486036628523073539">Will be disabled on 2022-02-02</a>
     */
    @Unofficial
    @Deprecated
    public void participate(ITwitchChat chat) {
        Map<String, Object> tags = new LinkedHashMap<>();
        tags.put(NONCE_TAG_NAME, CryptoUtils.generateNonce(32));
        tags.put(CHANT_MSG_ID_TAG_NAME, getMessageId());

        chat.sendMessage(channelName, text, tags);
    }

    /**
     * Attempts to parse the {@link ChatCrowdChant} information from a chat event.
     *
     * @param event the raw IRCMessageEvent.
     * @return ChatCrowdChant (or null if parsing was unsuccessful)
     */
    @Nullable
    public static ChatCrowdChant parse(IRCMessageEvent event) {
        String channelName = event.getChannelName().orElse(null);
        if (channelName == null) return null;

        String message = event.getMessage().orElse(null);
        if (message == null) return null;

        if ("crowd-chant".equals(event.getTags().get("msg-id"))) {
            return event.getMessageId()
                .map(id -> new ChatCrowdChant(id, message, true, channelName))
                .orElse(null);
        }

        return event.getTagValue(CHANT_MSG_ID_TAG_NAME)
            .map(id -> new ChatCrowdChant(id, message, false, channelName))
            .orElse(null);
    }

}
