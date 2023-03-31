package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatBadge {

    /**
     * An ID that identifies this version of the badge.
     * The ID can be any value.
     * For example, for Bits, the ID is the Bits tier level, but for World of Warcraft, it could be Alliance or Horde.
     */
    private String id;

    /**
     * A URL to the small version (18px x 18px) of the badge.
     */
    @JsonProperty("image_url_1x")
    private String smallImageUrl;

    /**
     * A URL to the medium version (36px x 36px) of the badge.
     */
    @JsonProperty("image_url_2x")
    private String mediumImageUrl;

    /**
     * A URL to the large version (72px x 72px) of the badge.
     */
    @JsonProperty("image_url_4x")
    private String largeImageUrl;

    /**
     * The title of the badge.
     */
    private String title;

    /**
     * The description of the badge.
     */
    private String description;

    /**
     * The action to take when clicking on the badge.
     * Set to null if no action is specified.
     * Examples include "subscribe_to_channel" and "visit_url"
     */
    @Nullable
    private String clickAction;

    /**
     * The URL to navigate to when clicking on the badge.
     * Set to null if no URL is specified.
     */
    @Nullable
    private String clickUrl;

}
