package io.twitch4j.events;

@FunctionalInterface
public interface IListener<T extends Event> {
    void handle(T event);
}
