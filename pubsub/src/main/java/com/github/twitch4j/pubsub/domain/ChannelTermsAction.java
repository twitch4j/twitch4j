package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChannelTermsAction {

    /**
     * The type of channel terms action that took place.
     */
    private Type type;

    /**
     * Unique id for the channel term.
     */
    private String id;

    /**
     * The ID of the channel where this action took place.
     */
    private String channelId;

    /**
     * The relevant text for this channel term.
     */
    private String text;

    /**
     * The user id of the executor of the channel terms action.
     */
    private String requesterId;

    /**
     * The user login name of the executor of the channel terms action.
     */
    private String requesterLogin;

    /**
     * UTC timestamp of when this term rule expires.
     */
    @Nullable
    private Instant expiresAt;

    /**
     * UTC timestamp of when this term was last updated.
     */
    private Instant updatedAt;

    /**
     * Whether the action was executed by Automod.
     */
    @Accessors(fluent = true)
    @JsonProperty("from_automod")
    private Boolean isFromAutomod;

    public enum Type {

        /**
         * Moderator added a blocked term to AutoMod.
         */
        ADD_BLOCKED_TERM,

        /**
         * Moderator added a permitted term to AutoMod.
         */
        ADD_PERMITTED_TERM,

        /**
         * Moderator deleted a blocked term from AutoMod.
         */
        DELETE_BLOCKED_TERM,

        /**
         * Moderator deleted a permitted term from AutoMod.
         */
        DELETE_PERMITTED_TERM

    }

}
