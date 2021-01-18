package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    private String id;

    @JsonProperty("edit_url")
    private String editUrl;
}
