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
public class UserUpdateEvent extends EventSubUserEvent {

    /**
     * The user’s email.
     * Only included if you have the user:read:email scope for the user.
     */
    private String email;

    /**
     * The user’s description.
     */
    private String description;

}
