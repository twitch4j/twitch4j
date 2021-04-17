package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EventSubUserChannelEvent extends EventSubEvent {

    /**
     * The requested broadcaster ID.
     */
    private String broadcasterUserId;

    /**
     * The requested broadcaster display name.
     */
    private String broadcasterUserName;

    /**
     * The requested broadcaster login name.
     */
    private String broadcasterUserLogin;

    /**
     * The user’s user id.
     */
    private String userId;

    /**
     * The user’s display name.
     */
    private String userName;

    /**
     * The user's login name.
     */
    private String userLogin;

}
