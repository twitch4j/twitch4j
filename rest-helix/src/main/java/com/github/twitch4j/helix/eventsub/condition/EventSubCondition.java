package com.github.twitch4j.helix.eventsub.condition;

import java.util.Map;

public abstract class EventSubCondition {

    public abstract Map<String, Object> toMap();

}
