package com.github.twitch4j.eventsub.socket.conduit.exceptions;

public final class ConduitResizeException extends Exception implements ConduitSocketPoolException {
    public ConduitResizeException(String conduitId, Exception cause) {
        super("Failed to expand size of Conduit with ID: " + conduitId, cause);
    }
}
