package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Data for creating Stream Markers. The {@code userId} of the streamer on whose 
 * stream to create the marker is required; the {@code description} is optional.
 */
@Data
public class Highlight {
    
    /**
     * ID of the broadcaster to create a marker for
     */
    @JsonProperty("user_id")
    private final String userId;
    
    /**
     * Optional description to include with the marker
     */
    private final String description;
    
    public Highlight(String userId) {
        this(userId, "");
    }
    
    public Highlight(String userId, String description) {
        this.userId = userId;
        if(description == null)
            this.description = "";
        else
            this.description = description;
    }
}
