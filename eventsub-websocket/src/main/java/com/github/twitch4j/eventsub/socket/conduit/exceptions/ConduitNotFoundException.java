package com.github.twitch4j.eventsub.socket.conduit.exceptions;

public final class ConduitNotFoundException extends Exception implements ConduitSocketPoolException {
    public ConduitNotFoundException(String conduitId, Exception cause) {
        super("Could not find existing Conduit for pool with ID: " + conduitId, cause);
    }
}
