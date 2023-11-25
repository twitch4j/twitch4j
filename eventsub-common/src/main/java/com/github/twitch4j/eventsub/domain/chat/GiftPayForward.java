package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.annotation.Unofficial;
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

    /**
     * The user id of the recipient of the paid-forward gift sub.
     * Null if gifted to the broad community, rather than a specific target.
     */
    @Nullable
    @Unofficial // https://github.com/twitchdev/issues/issues/868
    private String recipientUserId;

    /**
     * The user name of the recipient of the paid-forward gift sub.
     * Null if gifted to the broad community, rather than a specific target.
     */
    @Nullable
    @Unofficial // https://github.com/twitchdev/issues/issues/868
    private String recipientUserName;

    /**
     * The user login of the recipient of the paid-forward gift sub.
     * Null if gifted to the broad community, rather than a specific target.
     */
    @Nullable
    @Unofficial // https://github.com/twitchdev/issues/issues/868
    private String recipientUserLogin;

}
