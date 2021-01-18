package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModeratorEventList {

    @JsonProperty("data")
    private List<ModeratorEvent> events;

    @JsonProperty("pagination")
    private HelixPagination pagination;

    /**
     * @return the moderator events from this query
     * @deprecated in favor of getEvents()
     */
    @Deprecated
    public List<ModeratorEvent> getSubscriptions() {
        return this.events;
    }
}
