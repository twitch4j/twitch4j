package io.twitch4j.events;

import io.twitch4j.ITwitchClient;

public abstract class Event {
    public abstract ITwitchClient getClient();
}
