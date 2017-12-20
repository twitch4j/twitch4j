package me.philippheuer.twitch4j.api.operations;

import me.philippheuer.twitch4j.api.model.IUser;

import java.util.Optional;

public interface UsersOperation {
    IUser getUser();
    IUser getUser(long id);
    Optional<IUser> getUserByName(String username);
}
