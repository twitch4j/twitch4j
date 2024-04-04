package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class EventSubModeratorEvent extends EventSubChannelEvent implements ModeratorEvent {

    /**
     * The user ID of the moderator associated with this event.
     */
    private String moderatorUserId;

    /**
     * The user login of the moderator associated with this event.
     */
    private String moderatorUserLogin;

    /**
     * The user name of the  moderator associated with this event.
     */
    private String moderatorUserName;

}
