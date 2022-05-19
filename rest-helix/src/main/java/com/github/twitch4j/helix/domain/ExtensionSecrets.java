package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ExtensionSecrets {

    /**
     * Indicates the version associated with the Extension secrets in the response.
     */
    private Integer formatVersion;

    /**
     * The secret objects.
     */
    private List<ExtensionSecret> secrets;

}
