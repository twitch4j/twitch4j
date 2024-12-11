package com.github.twitch4j.eventsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class BlockedTermWrapper {

    /**
     * The list of blocked terms found in the message.
     */
    private List<BlockedTerm> termsFound;

}
