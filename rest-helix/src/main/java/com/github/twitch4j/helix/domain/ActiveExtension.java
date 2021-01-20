package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ActiveExtension {

    /**
     * Activation state of the extension, for each extension type (component, overlay, mobile, panel). If false, no other data is provided.
     */
    private Boolean active;

    /**
     * (Client) ID of the extension.
     */
    private String id;

    /**
     * Name of the extension.
     */
    private String name;

    /**
     * Version of the extension.
     */
    private String version;

    /**
     * (Video-component Extensions only) X-coordinate of the placement of the extension.
     */
    private Integer x;

    /**
     * (Video-component Extensions only) Y-coordinate of the placement of the extension.
     */
    private Integer y;

    Extension asExtension() {
        return new Extension(id, name, version, null, null);
    }

}
