package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.TwitchEnum;
import com.github.twitch4j.common.util.TwitchEnumDeserializer;
import com.github.twitch4j.eventsub.domain.AutomodCategory;
import com.github.twitch4j.eventsub.domain.AutomodMessageStatus;
import com.github.twitch4j.eventsub.domain.chat.Message;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class AutomodMessageUpdateEvent extends EventSubModerationEvent {

    /**
     * The ID of the message that was flagged by automod.
     */
    private String messageId;

    /**
     * The body of the message.
     */
    private Message message;

    /**
     * The category of the message offense.
     */
    @JsonDeserialize(using = TwitchEnumDeserializer.class)
    private TwitchEnum<AutomodCategory> category;

    /**
     * The level of severity. Measured between 1 to 4.
     */
    private int level;

    /**
     * The message's updated status.
     */
    private AutomodMessageStatus status;

    /**
     * The timestamp of when automod saved the message.
     */
    private Instant heldAt;

    /**
     * @return whether the message was held by AutoMod for containing a channel-specific blocked term
     */
    @Unofficial // not officially documented behavior
    public boolean containsBlockedTerm() {
        return "invalid".equals(category.getRawValue());
    }

}
