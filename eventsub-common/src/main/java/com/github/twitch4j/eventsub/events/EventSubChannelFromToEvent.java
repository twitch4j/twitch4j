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
public class EventSubChannelFromToEvent extends EventSubEvent {

    /**
     * The broadcaster ID that triggered the event.
     */
    private String fromBroadcasterUserId;

    /**
     * The broadcaster login that triggered the event.
     */
    private String fromBroadcasterUserLogin;

    /**
     * The broadcaster display name that triggered the event.
     */
    private String fromBroadcasterUserName;

    /**
     * The broadcaster ID that received the event.
     */
    private String toBroadcasterUserId;

    /**
     * The broadcaster login that received the event.
     */
    private String toBroadcasterUserLogin;

    /**
     * The broadcaster display name that received the event.
     */
    private String toBroadcasterUserName;

}
