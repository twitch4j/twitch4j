package com.github.twitch4j.eventsub.socket.conduit;

public class CreateConduitException extends Exception implements ConduitSocketPoolException {
    CreateConduitException(Exception cause) {
        super("Failed to create Conduit with helix for pool", cause);
    }
}
