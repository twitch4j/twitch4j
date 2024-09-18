package com.github.twitch4j.chat.events;

import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.events.channel.MirrorableEvent;
import com.github.twitch4j.chat.events.channel.ReplyableEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.Set;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractChannelMessageEvent extends AbstractChannelEvent implements ReplyableEvent, MirrorableEvent {

    /**
     * RAW Message Event
     */
    private IRCMessageEvent messageEvent;

    /**
     * User
     */
    private EventUser user;

    /**
     * Message
     */
    private String message;

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
    private final String nonce = getMessageEvent().getNonce().orElse(null);

    @ApiStatus.Internal
    public AbstractChannelMessageEvent(EventChannel channel, IRCMessageEvent messageEvent, EventUser user, String message) {
        super(channel);
        this.messageEvent = messageEvent;
        this.user = user;
        this.message = message;
        this.subscriberMonths = messageEvent.getSubscriberMonths().orElse(0);
        this.subscriptionTier = messageEvent.getSubscriptionTier().orElse(0);
    }

    /**
     * Permissions of the user
     */
    public Set<CommandPermission> getPermissions() {
        return messageEvent.getClientPermissions();
    }

    /**
     * @return the regions of the message that were flagged by AutoMod.
     */
    @Unofficial
    public List<AutoModFlag> getFlags() {
        return this.getMessageEvent().getFlags();
    }

}
