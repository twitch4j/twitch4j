package io.twitch4j.impl.events;

import io.twitch4j.IClient;
import io.twitch4j.events.IDispatcher;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventDispatcherImpl implements IDispatcher {
    private final IClient client;
}
