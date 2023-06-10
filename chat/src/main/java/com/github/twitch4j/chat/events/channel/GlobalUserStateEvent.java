package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.common.enums.CommandPermission;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = false)
public class GlobalUserStateEvent extends TwitchEvent {

    /**
     * Raw Message Event
     */
    IRCMessageEvent messageEvent;

    /**
     * @return the user's display name, or empty if it is never set.
     */
    public Optional<String> getDisplayName() {
        return messageEvent.getTagValue("display-name");
    }

    /**
     * @return the user's id, or empty if it is never set.
     */
    public Optional<String> getUserId() {
        return Optional.ofNullable(messageEvent.getUserId());
    }

    /**
     * @return hexadecimal RGB color code, or empty if it is never set.
     */
    public Optional<String> getColor() {
        return messageEvent.getUserChatColor();
    }

    /**
     * @return collection of emote sets.
     */
    public List<String> getEmoteSets() {
        return Collections.unmodifiableList(Arrays.asList(
            messageEvent.getTagValue("emote-sets")
                .map(emoteSets -> StringUtils.split(emoteSets, ','))
                .orElse(new String[0])
        ));
    }

    /**
     * @return whether the user is a staff member.
     */
    public boolean isStaff() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.TWITCHSTAFF);
    }

    /**
     * @return whether the user has a prime or turbo badge.
     */
    public boolean isPrimeOrTurbo() {
        return this.messageEvent.getClientPermissions().contains(CommandPermission.PRIME_TURBO);
    }

}
