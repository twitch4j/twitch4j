package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionSecretsList {

    /**
     * The extension secrets; each secret object contains a base64-encoded secret, a UTC timestamp when the secret becomes active, and a timestamp when the secret expires.
     */
    private List<ExtensionSecrets> data;

}
