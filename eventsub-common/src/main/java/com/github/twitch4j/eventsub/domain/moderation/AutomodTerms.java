package com.github.twitch4j.eventsub.domain.moderation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * Metadata associated with the automod terms changes.
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodTerms {

    /**
     * Whether the terms were added due to an Automod message approve/deny action.
     */
    @JsonProperty("from_automod")
    private boolean fromAutomod;

    /**
     * Whether the moderator action constitutes adding or removing a term.
     */
    private TermAction action;

    /**
     * Whether the modified term is associated with the blocked term list or permitted term list.
     */
    private TermType list;

    /**
     * The terms being added or removed.
     */
    private List<String> terms;

}
