package me.philippheuer.twitch4j.api.operations;

import java.util.Collection;

public interface Operation {
    <T> T get(String queryEndpoint, Object data);
    <T> Collection<T> getList(String queryEndpoint, Object data);
}
