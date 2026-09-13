package com.github.twitch4j.eventsub.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Gif {

    /**
     * An ID that uniquely identifies this GIF.
     */
    @JsonProperty("id") // docs were incorrect https://discord.com/channels/504015559252377601/523675960797691915/1544919248881721435
    private String gifId;

    /**
     * The URL of the GIF asset.
     * <p>
     * Applications rendering the GIF must use the full URL provided; it must not be modified (according to Twitch).
     */
    private String url;

}
