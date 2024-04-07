package com.github.twitch4j.eventsub.socket.conduit.exceptions;

public final class ShardTimeoutException extends InterruptedException implements ConduitSocketPoolException {
    public ShardTimeoutException(long timeout) {
        super(String.format("Websocket shard(s) were not welcomed by Twitch before the socket timeout of %d ms", timeout));
    }
}
