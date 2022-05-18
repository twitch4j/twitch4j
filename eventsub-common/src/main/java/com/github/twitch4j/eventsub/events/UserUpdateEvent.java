package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

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
     * A Boolean value that determines whether Twitch has verified the user’s email address.
     * Is true if Twitch has verified the email address; otherwise, false.
     * <p>
     * Note from Twitch: Ignore this field if the email field contains an empty string.
     */
    @Accessors(fluent = true)
    @JsonProperty("email_verified")
    private Boolean isEmailVerified;

    /**
     * The user’s description.
     */
    private String description;

}
