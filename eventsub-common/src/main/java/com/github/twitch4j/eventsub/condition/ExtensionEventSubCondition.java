package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
public class ExtensionEventSubCondition extends EventSubCondition {

    /**
     * The client ID of the extension.
     * The provided extensionClientId must match the client id in the access token.
     */
    private String extensionClientId;

}
