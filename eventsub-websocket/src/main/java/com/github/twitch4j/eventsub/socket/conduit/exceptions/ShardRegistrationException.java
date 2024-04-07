package com.github.twitch4j.eventsub.socket.conduit.exceptions;

public final class ShardRegistrationException extends Exception implements ConduitSocketPoolException {
    public ShardRegistrationException(String conduitId, Exception cause) {
        super("Failed to register shards for Conduit with ID: " + conduitId, cause);
    }
}
