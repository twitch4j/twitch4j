package com.github.twitch4j.eventsub.socket.conduit;

public class ConduitResizeException extends Exception implements ConduitSocketPoolException {
    ConduitResizeException(String conduitId, Exception cause) {
        super("Failed to expand size of Conduit with ID: " + conduitId, cause);
    }
}
