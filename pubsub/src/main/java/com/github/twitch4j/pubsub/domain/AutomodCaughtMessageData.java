package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodCaughtMessageData {

    private AutomodContentClassification contentClassification;
    private AutomodCaughtMessage message;
    private String reasonCode;
    private String resolverId;
    private String resolverLogin;
    private Status status;

    public enum Status {
        PENDING, @Deprecated APPROVED, ALLOWED, DENIED, EXPIRED, @JsonEnumDefaultValue INVALID
    }

}
