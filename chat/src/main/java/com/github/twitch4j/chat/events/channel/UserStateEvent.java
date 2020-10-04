package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventChannel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;

/**
 * This event gets called when a user joins a channel or sends a PRIVMSG to a channel.
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class UserStateEvent extends AbstractChannelEvent {

    /**
     * Hexadecimal RGB color code, or null if it is never set.
     */
    private String color;

    /**
     * The user's display name, or null if it is never set.
     */
    private String displayName;

    /**
     * List of emote sets.
     */
    private List<String> emoteSets;

    /**
     * Badges map of tags to versions.
     */
    private Map<String, String> badges;

    /**
     * Metadata related to the chat badges.
     */
    private Map<String, String> badgeInfo;

    /**
     * Event constructor
     *
     * @param channel     The channel that this event originates from.
     * @param displayName The display version of the user name
     * @param color       Hexadecimal RGB color code of the user
     * @param emoteSets   The emote sets the user is entitled to use.
     * @param badges      Chat badges of the user and the version of each badge
     * @param badgeInfo   Metadata related to the user chat badges
     */
    public UserStateEvent(EventChannel channel, String displayName, String color,
                          List<String> emoteSets, Map<String, String> badges, Map<String, String> badgeInfo) {
        super(channel);
        this.displayName = displayName;
        this.color = color;
        this.emoteSets = Collections.unmodifiableList(emoteSets);
        this.badges = Collections.unmodifiableMap(badges);
        this.badgeInfo = Collections.unmodifiableMap(badgeInfo);
    }

    /**
     * @return true if user is subscribed, false otherwise
     */
    public boolean isSubscriber() {
        return this.badges.containsKey("subscriber");
    }

    /**
     * @return number of months the user has been a subscriber, or empty if they are not subscribed
     */
    public OptionalInt getSubscriberMonth() {
        final String subscriberMonthsStr = this.badgeInfo.get("subscriber");

        if (subscriberMonthsStr != null) {
            try {
                return OptionalInt.of(Integer.parseInt(subscriberMonthsStr));
            } catch (Exception ignored) {
            }
        }

        return OptionalInt.empty();
    }

    /**
     * @return true if user is admin, false otherwise
     */
    public boolean isAdmin() {
        return this.badges.containsKey("admin");
    }

    /**
     * @return true if user is broadcaster, false otherwise
     */
    public boolean isBroadcaster() {
        return this.badges.containsKey("broadcaster");
    }

    /**
     * @return true if user is global mod, false otherwise
     */
    public boolean isGlobalMod() {
        return this.badges.containsKey("global_mod");
    }

    /**
     * @return true if user is moderator, false otherwise
     */
    public boolean isModerator() {
        return this.badges.containsKey("moderator");
    }

    /**
     * @return true if user is staff, false otherwise
     */
    public boolean isStaff() {
        return this.badges.containsKey("staff");
    }

    /**
     * @return true if user have turbo badge, false otherwise
     */
    public boolean isTurbo() {
        return this.badges.containsKey("turbo");
    }
}
