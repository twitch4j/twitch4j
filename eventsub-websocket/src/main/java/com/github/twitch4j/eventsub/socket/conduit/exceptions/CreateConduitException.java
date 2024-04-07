package com.github.twitch4j.eventsub.socket.conduit.exceptions;

public final class CreateConduitException extends Exception implements ConduitSocketPoolException {
    public CreateConduitException(Exception cause) {
        super("Failed to create Conduit with helix for pool", cause);
    }
}
