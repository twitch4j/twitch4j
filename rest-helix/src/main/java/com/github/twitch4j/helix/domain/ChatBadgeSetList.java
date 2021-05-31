package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ChatBadgeSetList {

    /**
     * The chat badge sets.
     */
    @JsonProperty("data")
    private List<ChatBadgeSet> badgeSets;

}
