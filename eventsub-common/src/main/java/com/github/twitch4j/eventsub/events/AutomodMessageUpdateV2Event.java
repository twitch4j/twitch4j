package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.domain.AutomodMessageStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class AutomodMessageUpdateV2Event extends AutomodMessageEvent implements ModeratorEvent {

    /**
     * The user ID of the issuer of the moderation action.
     */
    private String moderatorUserId;

    /**
     * The user login of the issuer of the moderation action.
     */
    private String moderatorUserLogin;

    /**
     * The user name of the issuer of the moderation action.
     */
    private String moderatorUserName;

    /**
     * The message's updated status.
     */
    private AutomodMessageStatus status;

}
