package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * This event gets called when a user joins a channel or sends a PRIVMSG to a channel.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserStateEvent extends AbstractChannelEvent {

    /**
     * RAW Message Event
     */
    private IRCMessageEvent messageEvent;

    /**
     * List of emote sets.
     */
    private List<String> emoteSets;

    /**
     * Event constructor
     *
     * @param messageEvent raw message event
     */
    public UserStateEvent(IRCMessageEvent messageEvent) {
        super(messageEvent.getChannel());
        this.messageEvent = messageEvent;
        String[] emoteSets = messageEvent.getTagValue("emote-sets")
            .map(emoteSetsStr -> emoteSetsStr.split(",")).orElse(new String[]{});
        this.emoteSets = Collections.unmodifiableList(Arrays.asList(emoteSets));
    }

    /**
     * @return Hexadecimal RGB color code, or empty if it is never set
     */
    public Optional<String> getColor() {
        return messageEvent.getUserChatColor();
    }

    /**
     * @return The user's display name, or empty if it is never set
     */
    public Optional<String> getDisplayName(){
        return messageEvent.getTagValue("display-name");
    }

    /**
     * @return true if user is subscribed, false otherwise
     */
    public boolean isSubscriber() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.SUBSCRIBER);
    }

    /**
     * @return number of months the user has been a subscriber, or empty if they are not subscribed
     */
    public OptionalInt getSubscriberMonth() {
        return this.messageEvent.getSubscriberMonths();
    }

    /**
     * @return true if user is broadcaster, false otherwise
     */
    public boolean isBroadcaster() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.BROADCASTER);
    }

    /**
     * @return true if user is moderator, false otherwise
     */
    public boolean isModerator() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.MODERATOR);
    }

    /**
     * @return true if user is staff, false otherwise
     */
    public boolean isStaff() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.TWITCHSTAFF);
    }

    /**
     * @return true if user have prime or turbo badge, false otherwise
     */
    public boolean isPrimeOrTurbo() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.PRIME_TURBO);
    }
}
