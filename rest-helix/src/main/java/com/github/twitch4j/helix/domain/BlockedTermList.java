package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class BlockedTermList {

    /**
     * The list of blocked terms.
     * <p>
     * The list is in descending order of when they were created (see the created_at timestamp).
     */
    @JsonProperty("data")
    private List<BlockedTerm> blockedTerms;

    /**
     * The information used to paginate the response data.
     */
    private HelixPagination pagination;

}
