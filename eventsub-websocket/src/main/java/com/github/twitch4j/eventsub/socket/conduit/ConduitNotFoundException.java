package com.github.twitch4j.eventsub.socket.conduit;

public class ConduitNotFoundException extends Exception implements ConduitSocketPoolException {
    ConduitNotFoundException(String conduitId, Exception cause) {
        super("Could not find existing Conduit for pool with ID: " + conduitId, cause);
    }
}
