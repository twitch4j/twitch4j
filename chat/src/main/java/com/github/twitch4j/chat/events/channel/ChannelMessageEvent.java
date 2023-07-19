package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelMessageEvent;
import com.github.twitch4j.chat.util.ChatCrowdChant;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.ChatReply;
import com.github.twitch4j.common.util.DonationAmount;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * This event gets called when a message is received in a channel.
 */
@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelMessageEvent extends AbstractChannelMessageEvent {

    /**
     * @see <a href="https://dev.twitch.tv/docs/irc/tags/#privmsg-tags">Documentation</a>
     */
    private static final Map<String, Integer> HYPE_CHAT_LEVELS;

    /**
     * @see <a href="https://help.twitch.tv/s/article/hype-chat-by-twitch">Documentation</a>
     */
    private static final Duration[] HYPE_CHAT_DURATION;

    /**
     * Information regarding the parent message being replied to, if applicable.
     */
    @Nullable
    @Getter(lazy = true)
    ChatReply replyInfo = ChatReply.parse(getMessageEvent().getEscapedTags());

    /**
     * Information regarding any associated Crowd Chant for this message, if applicable.
     *
     * @deprecated <a href="https://twitter.com/TwitchSupport/status/1486036628523073539">Will be disabled on 2022-02-02</a>
     */
    @Nullable
    @Unofficial
    @Getter(lazy = true)
    @Deprecated
    ChatCrowdChant chantInfo = ChatCrowdChant.parse(getMessageEvent());

    /**
     * Event Constructor
     *
     * @param channel      The channel that this event originates from.
     * @param messageEvent The raw message event
     * @param user         The user who triggered the event.
     * @param message      The plain text of the message.
     * @param permissions  The permissions of the triggering user.
     */
    public ChannelMessageEvent(
        EventChannel channel,
        IRCMessageEvent messageEvent,
        EventUser user, String message,
        Set<CommandPermission> permissions
    ) {
        super(channel, messageEvent, user, message, permissions);
    }

    /**
     * @return whether "Highlight My Message" was redeemed for this event
     */
    @Unofficial
    public boolean isHighlightedMessage() {
        return StringUtils.equals("highlighted-message", getMessageEvent().getRawTag("msg-id"));
    }

    /**
     * @return whether "Send a Message in Sub-Only Mode" was redeemed for this event
     */
    @Unofficial
    public boolean isSkipSubsModeMessage() {
        return StringUtils.equals("skip-subs-mode-message", getMessageEvent().getRawTag("msg-id"));
    }

    /**
     * @return the id for the custom reward that was redeemed with this associated message, in an optional wrapper
     */
    @Unofficial
    public Optional<String> getCustomRewardId() {
        return getMessageEvent().getTagValue("custom-reward-id");
    }

    /**
     * @return whether this is the user's first message in the channel.
     * @apiNote This method is marked as unofficial since this tag does not appear in the irc guide.
     * @see <a href="https://help.twitch.tv/s/article/first-time-chatter-highlight">Official documentation</a>
     */
    @Unofficial
    public boolean isDesignatedFirstMessage() {
        return StringUtils.equals("1", getMessageEvent().getRawTag("first-msg"));
    }

    /**
     * @return whether this message constitutes the user's designated introduction.
     * @apiNote This method is unofficial since the experiment is not officially documented in the irc guide.
     * @see <a href="https://twitter.com/TwitchSupport/status/1481008097749573641">Twitch Announcement</a>
     */
    @Unofficial
    public boolean isUserIntroduction() {
        return StringUtils.equals("user-intro", getMessageEvent().getRawTag("msg-id"));
    }

    /**
     * Hype Chat Contribution
     *
     * @return the payment information related to this hype chat, if applicable.
     * @see <a href ="https://blog.twitch.tv/en/2023/06/22/introducing-hype-chat-a-new-way-to-stand-out/">Twitch Announcement</a>
     */
    public Optional<DonationAmount> getElevatedChatPayment() {
        final Map<String, CharSequence> tags = getMessageEvent().getEscapedTags();

        CharSequence amount = tags.get("pinned-chat-paid-amount");
        if (amount == null) {
            amount = tags.get("pinned-chat-paid-canonical-amount");
        }

        return Optional.ofNullable(amount)
            .map(amt -> {
                try {
                    return Long.parseLong(amt.toString());
                } catch (Exception e) {
                    return null;
                }
            })
            .map(amt -> {
                String currency = tags.getOrDefault("pinned-chat-paid-currency", "USD").toString();
                String exponentStr = tags.getOrDefault("pinned-chat-paid-exponent", "2").toString();
                int exponent;
                try {
                    exponent = Integer.parseInt(exponentStr);
                } catch (Exception e) {
                    return null;
                }
                return new DonationAmount(amt, exponent, currency);
            });
    }

    /**
     * @return the level associated with the hype chat contribution (determines how long the paid message is pinned).
     * @see #getElevatedChatPayment()
     */
    public Optional<Integer> getHypeChatLevel() {
        return getMessageEvent()
            .getTagValue("pinned-chat-paid-level")
            .map(HYPE_CHAT_LEVELS::get);
    }

    /**
     * @return the amount of time this hype chat message is pinned to the top of chat.
     * @see #getHypeChatLevel()
     */
    @ApiStatus.Experimental // in case twitch allows channels to modify the default durations
    public Optional<Duration> getHypeChatPinDuration() {
        return getHypeChatLevel()
            .filter(i -> i >= 1 && i <= HYPE_CHAT_DURATION.length)
            .map(i -> HYPE_CHAT_DURATION[i - 1]);
    }

    static {
        Map<String, Integer> levels = new HashMap<>();
        levels.put("ONE", 1);
        levels.put("TWO", 2);
        levels.put("THREE", 3);
        levels.put("FOUR", 4);
        levels.put("FIVE", 5);
        levels.put("SIX", 6);
        levels.put("SEVEN", 7);
        levels.put("EIGHT", 8);
        levels.put("NINE", 9);
        levels.put("TEN", 10);
        HYPE_CHAT_LEVELS = Collections.unmodifiableMap(levels);

        HYPE_CHAT_DURATION = new Duration[] {
            Duration.ofSeconds(30L),
            Duration.ofSeconds(150L),
            Duration.ofMinutes(5L),
            Duration.ofMinutes(10L),
            Duration.ofMinutes(30L),
            Duration.ofHours(1L),
            Duration.ofHours(2L),
            Duration.ofHours(3L),
            Duration.ofHours(4L),
            Duration.ofHours(5L)
        };
    }
}
