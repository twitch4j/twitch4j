package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatBadge {

    /**
     * ID of the chat badge version.
     */
    private String id;

    /**
     * Small image URL.
     */
    @JsonProperty("image_url_1x")
    private String smallImageUrl;

    /**
     * Medium image URL.
     */
    @JsonProperty("image_url_2x")
    private String mediumImageUrl;

    /**
     * Large image URL.
     */
    @JsonProperty("image_url_4x")
    private String largeImageUrl;

}
