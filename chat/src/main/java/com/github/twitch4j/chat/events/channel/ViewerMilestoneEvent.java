package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.AbstractChannelEvent;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

@Value
@Unofficial
@ApiStatus.Experimental
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ViewerMilestoneEvent extends AbstractChannelEvent {

    public static final String USERNOTICE_ID = "viewermilestone";

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    IRCMessageEvent messageEvent;

    EventUser user;

    String systemMessage; // e.g., "DisplayName watched 3 consecutive streams this month and sparked a watch streak!"

    String milestoneUniqueId; // e.g., "c2eb863f-317e-440f-9a69-fc149b1abfcc"

    String milestoneCategory; // e.g., "watch-streak"

    String milestoneValue; // e.g., "3"

    Integer earnedChannelPoints; // e.g., 450

    @ApiStatus.Internal
    public ViewerMilestoneEvent(@NotNull IRCMessageEvent messageEvent) {
        super(messageEvent.getChannel());
        this.messageEvent = messageEvent;
        this.user = messageEvent.getUser();
        this.systemMessage = messageEvent.getTagValue("system-msg").orElse(null);
        this.milestoneUniqueId = messageEvent.getTagValue("msg-param-id").orElse(null);
        this.milestoneCategory = messageEvent.getTagValue("msg-param-category").orElse(null);
        this.milestoneValue = messageEvent.getTagValue("msg-param-value").orElse(null);
        this.earnedChannelPoints = messageEvent.getTagValue("msg-param-copoReward").map(points -> {
            try {
                return Integer.parseInt(points);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }).orElse(null);
    }

    public String getUserMessage() {
        return messageEvent.getMessage().orElse("");
    }

    public boolean isWatchStreak() {
        return "watch-streak".equalsIgnoreCase(milestoneCategory);
    }

    public OptionalInt parseValue() {
        try {
            return OptionalInt.of(Integer.parseInt(milestoneValue));
        } catch (Exception e) {
            return OptionalInt.empty();
        }
    }

}
