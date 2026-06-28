package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;

@Value
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ModiversaryEvent extends AbstractChannelEvent {

    public static String USERNOTICE_ID = "modiversary";

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    EventUser moderator;

    String systemMessage; // e.g., "has been a moderator for 24 months!"

    /**
     * The total number of months the user has been a moderator in this channel.
     */
    Integer months;

    @ApiStatus.Internal
    public ModiversaryEvent(IRCMessageEvent event) {
        super(event.getChannel());
        this.messageEvent = event;
        this.moderator = event.getUser();
        this.systemMessage = event.getTagValue("system-msg").orElse(null);
        this.months = event.getTagValue("msg-param-months")
            .map(s -> {
                try {
                    return Integer.parseInt(s);
                } catch (NumberFormatException ignored) {
                    return null;
                }
            })
            .orElse(null);
    }

    public String getUserMessage() {
        return messageEvent.getMessage().orElse("");
    }

}
