package me.philippheuer.twitch4j.api.helix.operations;

public interface WebHooksOperation {
    void subscribeFollowers(long id);
    void subscribeFollowing(long id);
}
