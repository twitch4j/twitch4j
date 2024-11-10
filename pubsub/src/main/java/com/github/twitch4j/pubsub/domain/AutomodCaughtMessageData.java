package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodCaughtMessageData {

    /**
     * Object defining the category and level that the content was classified as.
     */
    private AutomodContentClassification contentClassification;

    /**
     * The message that was caught with additional context.
     */
    private AutomodCaughtMessage message;

    /**
     * Reserved for internal use by Twitch.
     */
    @Unofficial
    private String reasonCode;

    /**
     * User ID of the moderator that resolved this message.
     */
    private String resolverId;

    /**
     * User login name of the moderator that resolved this message.
     */
    private String resolverLogin;

    /**
     * Current status of the message.
     */
    private Status status;

    /**
     * The reason for AutoMod catching the message.
     */
    @Unofficial
    private CaughtMessageReason caughtMessageReason;

    public enum Status {
        PENDING, @Deprecated APPROVED, ALLOWED, DENIED, EXPIRED, @JsonEnumDefaultValue INVALID
    }

}
