package com.github.twitch4j.eventsub.condition;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@Jacksonized
public class ConduitCondition extends ApplicationEventSubCondition {

    /**
     * The conduit ID to receive events for.
     * If omitted, events for all of this client’s conduits are sent.
     */
    @Nullable
    private String conduitId;

}
