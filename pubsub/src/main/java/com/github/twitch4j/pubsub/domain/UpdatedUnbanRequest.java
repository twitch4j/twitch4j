package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UpdatedUnbanRequest extends UnbanRequest {

    private String resolverId;

    private String resolverLogin;

    private String resolverMessage;

    private String status; // e.g. "CANCELED" or "APPROVED" or "DENIED"

}
