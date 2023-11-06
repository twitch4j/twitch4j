package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

/**
 * Metadata for when a user (who has a gifted sub) gifts a sub to another user.
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class GiftPayForward {

    /**
     * Whether the gift was given anonymously.
     */
    @Accessors(fluent = true)
    @JsonProperty("gifter_is_anonymous")
    private Boolean wasGifterAnonymous;

    /**
     * The user ID of the user who originally gifted the subscription. Null if anonymous.
     */
    @Nullable
    private String gifterUserId;

    /**
     * The user name of the user who originally gifted the subscription. Null if anonymous.
     */
    @Nullable
    private String gifterUserName;

    /**
     * The user login of the user who originally gifted the subscription. Null if anonymous.
     */
    @Nullable
    private String gifterUserLogin;

}
