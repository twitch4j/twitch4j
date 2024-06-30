package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * @deprecated Twitch decommissioned this API; please migrate to EventSub
 */
@Data
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public class ModeratorEventList {

    @JsonProperty("data")
    private List<ModeratorEvent> events;

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
