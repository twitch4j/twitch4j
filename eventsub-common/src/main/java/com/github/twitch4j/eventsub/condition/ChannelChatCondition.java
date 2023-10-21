package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
public class ChannelChatCondition extends EventSubCondition {

    /**
     * User ID of the channel to receive chat events for.
     */
    private String broadcasterUserId;

    /**
     * The user ID to read chat as.
     */
    private String userId;

}
