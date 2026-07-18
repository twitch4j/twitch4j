package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Gif {

    /**
     * An ID that uniquely identifies this GIF.
     */
    private String gifId;

    /**
     * The URL of the GIF asset.
     * <p>
     * Applications rendering the GIF must use the full URL provided; it must not be modified (according to Twitch).
     */
    private String url;

}
