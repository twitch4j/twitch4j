package io.twitch4j.events;

public interface IDispatcher {
    void dispatch(Event event);
}
