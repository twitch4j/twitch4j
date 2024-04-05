package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static org.apache.commons.lang3.StringUtils.defaultString;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@Jacksonized
@ToString(callSuper = true)
public class ModeratorEventSubCondition extends ChannelEventSubCondition {

    /**
     * The ID of the broadcaster or one of the broadcaster’s moderators.
     * <p>
     * For webhooks, the user must have granted your app (client ID) the relevant permissions for the subscription type.
     * For websockets, the ID must match the user ID in the user access token.
     */
    private String moderatorUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModeratorEventSubCondition)) return false;
        if (!super.equals(o)) return false;

        ModeratorEventSubCondition that = (ModeratorEventSubCondition) o;
        return defaultString(moderatorUserId).equals(defaultString(that.moderatorUserId));
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + defaultString(moderatorUserId).hashCode();
    }
}
