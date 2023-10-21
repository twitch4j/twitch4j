package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class GiftPaidUpgrade {

    /**
     * Whether the gift was given anonymously.
     */
    @Accessors(fluent = true)
    @JsonProperty("gifter_is_anonymous")
    private Boolean wasGifterAnonymous;

    /**
     * The user ID of the user who gifted the subscription. Null if anonymous.
     */
    @Nullable
    private String gifterUserId;

    /**
     * The user name of the user who gifted the subscription. Null if anonymous.
     */
    @Nullable
    private String gifterUserName;

    /**
     * The user login of the user who gifted the subscription. Null if anonymous.
     */
    @Nullable
    private String gifterUserLogin;

}
