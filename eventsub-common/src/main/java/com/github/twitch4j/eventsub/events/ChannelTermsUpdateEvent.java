package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.TermUpdateType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelTermsUpdateEvent extends EventSubModeratorEvent {

    /**
     * The status change applied to the terms.
     */
    private TermUpdateType action;

    /**
     * Whether this term was added due to an Automod message approve/deny action.
     */
    @Accessors(fluent = true)
    @JsonProperty("from_automod")
    private Boolean isFromAutomod;

    /**
     * The terms that had a status change.
     */
    private List<String> terms;

}
