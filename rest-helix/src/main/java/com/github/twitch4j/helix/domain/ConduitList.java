package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.Conduit;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ConduitList {

    /**
     * Information about the client’s conduits.
     */
    @JsonProperty("data")
    private List<Conduit> conduits;

}
