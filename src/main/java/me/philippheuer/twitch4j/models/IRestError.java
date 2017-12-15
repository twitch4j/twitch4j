package me.philippheuer.twitch4j.models;

public interface IRestError {
    String getError();
    String getMessage();
    int getStatusCode();
}
