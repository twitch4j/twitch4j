package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EventSubUserFromToEvent extends EventSubEvent {

    /**
     * The user ID that triggered the event.
     */
    private String fromUserId;

    /**
     * The user login name that triggered the event.
     */
    private String fromUserLogin;

    /**
     * The user display name that triggered the event.
     */
    private String fromUserName;

    /**
     * The user ID that received the event.
     */
    private String toUserId;

    /**
     * The user login name that received the event.
     */
    private String toUserLogin;

    /**
     * The user display name that received the event.
     */
    private String toUserName;

}
