package me.philippheuer.twitch4j.models;

public interface IWebSocketClient {
    void connect() throws Exception;
    void disconnect() throws Exception;
    void reconnect() throws Exception;
}
