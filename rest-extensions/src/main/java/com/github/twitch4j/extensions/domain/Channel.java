package com.github.twitch4j.extensions.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    /**
     * User id of the channel owner.
     */
    private String id;

    /**
     * Display name of the channel owner.
     */
    private String username;

    /**
     * Game Id.
     */
    private String game;

    /**
     * Stream title.
     */
    private String title;

    /**
     * Stream view count.
     *
     * @deprecated No migration path in the new Helix API.
     */
    @Deprecated
    private Long viewCount;

}
