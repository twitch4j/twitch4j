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
     * <p>
     * The list is sorted in ascending order by {@link ChatBadgeSet#getSetId()}.
     */
    @JsonProperty("data")
    private List<ChatBadgeSet> badgeSets;

}
