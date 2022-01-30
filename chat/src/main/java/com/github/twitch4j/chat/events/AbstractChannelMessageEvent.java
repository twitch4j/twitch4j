package com.github.twitch4j.chat.events;

import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import com.github.twitch4j.chat.events.channel.ReplyableEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class AbstractChannelMessageEvent extends AbstractChannelEvent implements ReplyableEvent {
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


	public AbstractChannelMessageEvent(EventChannel channel, IRCMessageEvent messageEvent, EventUser user, String message, Set<CommandPermission> permissions) {
        super(channel);
        this.messageEvent = messageEvent;
        this.user = user;
        this.message = message;
        this.permissions = permissions;
        this.subscriberMonths = messageEvent.getSubscriberMonths().orElse(0);
        this.subscriptionTier = messageEvent.getSubscriptionTier().orElse(0);
	}

    /**
     * @return the regions of the message that were flagged by AutoMod.
     */
    @Unofficial
    public List<AutoModFlag> getFlags() {
        return this.getMessageEvent().getFlags();
    }
}
