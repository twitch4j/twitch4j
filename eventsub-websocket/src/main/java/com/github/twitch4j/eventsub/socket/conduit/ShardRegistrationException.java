package com.github.twitch4j.eventsub.socket.conduit;

public class ShardRegistrationException extends Exception implements ConduitSocketPoolException {
    ShardRegistrationException(String conduitId, Exception cause) {
        super("Failed to register shards for Conduit with ID: " + conduitId, cause);
    }
}
