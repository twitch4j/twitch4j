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
public class ModeratorEventSubCondition extends ChannelEventSubCondition {

    /**
     * The ID of the broadcaster or one of the broadcaster’s moderators.
     * <p>
     * For webhooks, the user must have granted your app (client ID) the relevant permissions for the subscription type.
     * For websockets, the ID must match the user ID in the user access token.
     */
    private String moderatorUserId;

}
