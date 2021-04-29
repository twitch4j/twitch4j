package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ModeratorList {

    @JsonProperty("data")
    private List<Moderator> moderators;

    private HelixPagination pagination;

    /**
     * @return the moderators from this query
     * @deprecated in favor of getModerators()
     */
    @Deprecated
    public List<Moderator> getSubscriptions() {
        return this.moderators;
    }
}
