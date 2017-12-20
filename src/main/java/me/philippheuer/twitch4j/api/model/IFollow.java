package me.philippheuer.twitch4j.api.model;

import java.util.Date;

public interface IFollow {
    IChannel getChannel();
    Date createdAt();
    boolean hasNotifications();
    IUser getUser();
}
