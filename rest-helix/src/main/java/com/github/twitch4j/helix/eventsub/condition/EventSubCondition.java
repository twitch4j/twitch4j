package com.github.twitch4j.helix.eventsub.condition;

import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
public abstract class EventSubCondition {

    public abstract Map<String, Object> toMap();

}
