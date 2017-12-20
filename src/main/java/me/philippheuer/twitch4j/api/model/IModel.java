package me.philippheuer.twitch4j.api.model;

import me.philippheuer.twitch4j.IClient;

import java.util.Date;

public interface IModel {
    IClient getClient();
    Date createdAt();
    Date updatedAt();
}
