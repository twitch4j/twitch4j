package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clip Data
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateClip {

    /**
     * ID of the clip that was created.
     */
    private String id;

    /**
     * URL of the edit page for the clip.
     */
    private String editUrl;

}
