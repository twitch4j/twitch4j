package me.philippheuer.twitch4j.api.model;

import org.springframework.social.NotAuthorizedException;

public interface IUser extends IModel {
    String getStringId();
    long getLongId();
    String getUsername();
    String getDisplayName();
    String getLogoUrl();
    String getEmail() throws NotAuthorizedException;

    IChannel getChannel();

    boolean isFollowing(IChannel channel);
}
