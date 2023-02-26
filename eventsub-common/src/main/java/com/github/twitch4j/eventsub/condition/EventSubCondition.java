package com.github.twitch4j.eventsub.condition;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class EventSubCondition {}
