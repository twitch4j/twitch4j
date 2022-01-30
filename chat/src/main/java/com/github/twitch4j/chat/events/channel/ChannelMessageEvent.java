package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.chat.util.ChatCrowdChant;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.ChatReply;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This event gets called when a message is received in a channel.
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ChannelMessageEvent extends AbstractChannelEvent implements ReplyableEvent {

    /**
     * RAW Message Event
     */
    private final IRCMessageEvent messageEvent;

    /**
     * User
     */
    private EventUser user;

    /**
     * Message
     */
    private String message;

    /**
     * Permissions of the user
     */
    private Set<CommandPermission> permissions;

    /**
     * The exact number of months the user has been a subscriber, or zero if not subscribed
     */
    private int subscriberMonths;

    /**
     * The tier at which the user is subscribed (prime is treated as 1), or zero if not subscribed
     */
    private int subscriptionTier;

    /**
     * Nonce
     */
    @Unofficial
    @Getter(lazy = true)
    private String nonce = getMessageEvent().getNonce().orElse(null);

    /**
     * Information regarding the parent message being replied to, if applicable.
     */
    @Nullable
    @Getter(lazy = true)
    private ChatReply replyInfo = ChatReply.parse(getMessageEvent().getTags());

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
    public ChannelMessageEvent(EventChannel channel, IRCMessageEvent messageEvent, EventUser user, String message, Set<CommandPermission> permissions) {
        super(channel);
        this.messageEvent = messageEvent;
        this.user = user;
        this.message = message;
        this.permissions = permissions;
        this.subscriberMonths = messageEvent.getSubscriberMonths().orElse(0);
        this.subscriptionTier = messageEvent.getSubscriptionTier().orElse(0);
    }

    /**
     * @return whether "Highlight My Message" was redeemed for this event
     */
    @Unofficial
    public boolean isHighlightedMessage() {
        return "highlighted-message".equals(getMessageEvent().getTags().get("msg-id"));
    }

    /**
     * @return whether "Send a Message in Sub-Only Mode" was redeemed for this event
     */
    @Unofficial
    public boolean isSkipSubsModeMessage() {
        return "skip-subs-mode-message".equals(getMessageEvent().getTags().get("msg-id"));
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
        return "1".equals(getMessageEvent().getTags().get("first-msg"));
    }

    /**
     * @return whether this message constitutes the user's designated introduction.
     * @apiNote This method is unofficial since the experiment is not officially documented in the irc guide.
     * @see <a href="https://twitter.com/TwitchSupport/status/1481008097749573641">Twitch Announcement</a>
     */
    @Unofficial
    public boolean isUserIntroduction() {
        return "user-intro".equals(getMessageEvent().getTags().get("msg-id"));
    }

    /**
     * @return the regions of the message that were flagged by AutoMod.
     */
    @Unofficial
    public List<AutoModFlag> getFlags() {
        return this.messageEvent.getFlags();
    }

}
