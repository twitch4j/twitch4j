package io.twitch4j;

public interface ISocket {
    void connect();

    void disconnect();

    void reconnect();
}
