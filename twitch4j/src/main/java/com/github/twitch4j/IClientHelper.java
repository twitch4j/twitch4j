package com.github.twitch4j;

import com.github.twitch4j.chat.events.channel.FollowEvent;
import com.github.twitch4j.common.util.CollectionUtils;
import com.github.twitch4j.common.util.ExponentialBackoffStrategy;
import com.github.twitch4j.domain.ChannelCache;
import com.github.twitch4j.events.ChannelFollowCountUpdateEvent;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.helix.domain.UserList;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.twitch4j.TwitchClientHelper.MAX_LIMIT;
import static com.github.twitch4j.TwitchClientHelper.log;

public interface IClientHelper extends AutoCloseable {

    TwitchHelix getTwitchHelix();

    /**
     * Enable StreamEvent Listener, without invoking a Helix API call
     *
     * @param channelId   Channel Id
     * @param channelName Channel Name
     * @return true if the channel was added, false otherwise
     */
    boolean enableStreamEventListener(String channelId, String channelName);

    /**
     * Disable StreamEventListener, without invoking a Helix API call
     *
     * @param channelId Channel Id
     * @return true if the channel was removed, false otherwise
     */
    boolean disableStreamEventListenerForId(String channelId);

    /**
     * Enable Follow Listener, without invoking a Helix API call
     * <p>
     * For {@link FollowEvent} to fire, defaultAuthToken must be a user access token from a moderator of the channel
     * with the {@link com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ} scope.
     * Otherwise, the client helper can only fire {@link ChannelFollowCountUpdateEvent}
     * due to Twitch restrictions implemented on 2023-09-12.
     *
     * @param channelId   Channel Id
     * @param channelName Channel Name
     * @return true if the channel was added, false otherwise
     */
    boolean enableFollowEventListener(String channelId, String channelName);

    /**
     * Disable Follow Listener, without invoking a Helix API call
     *
     * @param channelId Channel Id
     * @return true when a previously-tracked channel was removed, false otherwise
     */
    boolean disableFollowEventListenerForId(String channelId);

    /**
     * Enable Clip Creation Listener, without invoking a Helix API call, starting at a custom timestamp
     *
     * @param channelId   Channel Id
     * @param channelName Channel Name
     * @param startedAt   The oldest clip creation timestamp to start the queries at
     * @return whether the channel was added
     */
    boolean enableClipEventListener(String channelId, String channelName, Instant startedAt);

    /**
     * Disable Clip Creation Listener, without invoking a Helix API call
     *
     * @param channelId Channel Id
     * @return whether a previously-tracked channel was removed
     */
    boolean disableClipEventListenerForId(String channelId);

    /**
     * Get cached information for a channel's stream status and follower count.
     * <p>
     * For this information to be valid, the respective event listeners need to be enabled for the channel.
     * <p>
     * For thread safety, the setters on this object should not be used; only getters.
     *
     * @param channelId The ID of the channel whose cache is to be retrieved.
     * @return ChannelCache in an optional wrapper.
     */
    Optional<ChannelCache> getCachedInformation(String channelId);

    /**
     * Updates {@link ExponentialBackoffStrategy#getBaseMillis()} for each of the independent listeners (i.e. stream status and followers)
     *
     * @param threadDelay the minimum milliseconds <i>delay</i> between each api call
     */
    void setThreadDelay(long threadDelay);

    /**
     * Enable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    @Nullable
    default User enableStreamEventListener(String channelName) {
        UserList users = getTwitchHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            User user = users.getUsers().get(0);
            if (enableStreamEventListener(user.getId(), user.getLogin()))
                return user;
        } else {
            log.error("Failed to add channel {} to stream event listener!", channelName);
        }

        return null;
    }

    /**
     * Enable StreamEvent Listener for the given channel names
     *
     * @param channelNames the channel names to be added
     */
    default Collection<User> enableStreamEventListener(Iterable<String> channelNames) {
        return CollectionUtils.chunked(channelNames, MAX_LIMIT).stream()
            .map(channels -> getTwitchHelix().getUsers(null, null, channels).execute())
            .map(UserList::getUsers)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(user -> enableStreamEventListener(user.getId(), user.getLogin()))
            .collect(Collectors.toList());
    }

    /**
     * Disable StreamEvent Listener
     *
     * @param channelName Channel Name
     */
    default void disableStreamEventListener(String channelName) {
        UserList users = getTwitchHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> disableStreamEventListenerForId(user.getId()));
        } else {
            log.error("Failed to remove channel " + channelName + " from stream event listener!");
        }
    }

    /**
     * Disable StreamEvent Listener for the given channel names
     *
     * @param channelNames the channel names to be removed
     */
    default void disableStreamEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = getTwitchHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> disableStreamEventListenerForId(user.getId()));
        });
    }

    /**
     * Enable Follow Listener for the given channel name
     * <p>
     * For {@link FollowEvent} to fire, defaultAuthToken must be a user access token from a moderator of the channel
     * with the {@link com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ} scope.
     * Otherwise, the client helper can only fire {@link ChannelFollowCountUpdateEvent}
     * due to Twitch restrictions implemented on 2023-09-12.
     *
     * @param channelName Channel Name
     */
    @Nullable
    default User enableFollowEventListener(String channelName) {
        UserList users = getTwitchHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            User user = users.getUsers().get(0);
            if (enableFollowEventListener(user.getId(), user.getLogin()))
                return user;
        } else {
            log.error("Failed to add channel " + channelName + " to Follow Listener, maybe it doesn't exist!");
        }

        return null;
    }

    /**
     * Enable Follow Listener for the given channel names
     * <p>
     * For {@link FollowEvent} to fire, defaultAuthToken must be a user access token from a moderator of the channel
     * with the {@link com.github.twitch4j.auth.domain.TwitchScopes#HELIX_CHANNEL_FOLLOWERS_READ} scope.
     * Otherwise, the client helper can only fire {@link ChannelFollowCountUpdateEvent}
     * due to Twitch restrictions implemented on 2023-09-12.
     *
     * @param channelNames the channel names to be added
     */
    default Collection<User> enableFollowEventListener(Iterable<String> channelNames) {
        return CollectionUtils.chunked(channelNames, MAX_LIMIT).stream()
            .map(channels -> getTwitchHelix().getUsers(null, null, channels).execute())
            .map(UserList::getUsers)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(user -> enableFollowEventListener(user.getId(), user.getLogin()))
            .collect(Collectors.toList());
    }

    /**
     * Disable Follow Listener
     *
     * @param channelName Channel Name
     */
    default void disableFollowEventListener(String channelName) {
        UserList users = getTwitchHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            users.getUsers().forEach(user -> disableFollowEventListenerForId(user.getId()));
        } else {
            log.error("Failed to remove channel " + channelName + " from follow listener!");
        }
    }

    /**
     * Disable Follow Listener for the given channel names
     *
     * @param channelNames the channel names to be removed
     */
    default void disableFollowEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = getTwitchHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> disableFollowEventListenerForId(user.getId()));
        });
    }

    /**
     * Enable Clip Creation Listener, without invoking a Helix API call
     *
     * @param channelId   Channel Id
     * @param channelName Channel Name
     * @return whether the channel was added
     */
    default boolean enableClipEventListener(String channelId, String channelName) {
        return enableClipEventListener(channelId, channelName, Instant.now());
    }

    /**
     * Clip Creation Listener
     *
     * @param channelName Channel Name
     * @return the channel whose clip creations are now tracked, or null (if unable to resolve or already tracking)
     */
    @Nullable
    default User enableClipEventListener(String channelName) {
        UserList users = getTwitchHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            User user = users.getUsers().get(0);
            if (enableClipEventListener(user.getId(), user.getLogin()))
                return user;
        } else {
            log.error("Failed to add channel " + channelName + " to Clip Creation Listener, maybe it doesn't exist!");
        }

        return null;
    }

    /**
     * Enable Clip Creation Listener for the given channel names
     *
     * @param channelNames the channel names to be added
     * @return the channels that are freshly tracked for clip creations
     */
    default Collection<User> enableClipEventListener(Iterable<String> channelNames) {
        return CollectionUtils.chunked(channelNames, MAX_LIMIT).stream()
            .map(channels -> getTwitchHelix().getUsers(null, null, channels).execute())
            .map(UserList::getUsers)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(user -> enableClipEventListener(user.getId(), user.getLogin()))
            .collect(Collectors.toList());
    }

    /**
     * Disable Clip Creation Listener
     *
     * @param channelName Channel Name
     * @return whether a previously-tracked channel was removed
     */
    default boolean disableClipEventListener(String channelName) {
        UserList users = getTwitchHelix().getUsers(null, null, Collections.singletonList(channelName)).execute();

        if (users.getUsers().size() == 1) {
            return disableClipEventListenerForId(users.getUsers().get(0).getId());
        } else {
            log.error("Failed to remove channel " + channelName + " from clip creation listener!");
            return false;
        }
    }

    /**
     * Disable Clip Creation Listener for the given channel names
     *
     * @param channelNames the channel names to be removed
     */
    default void disableClipEventListener(Iterable<String> channelNames) {
        CollectionUtils.chunked(channelNames, MAX_LIMIT).forEach(channels -> {
            UserList users = getTwitchHelix().getUsers(null, null, channels).execute();
            users.getUsers().forEach(user -> disableClipEventListenerForId(user.getId()));
        });
    }

    /**
     * Updates {@link ExponentialBackoffStrategy#getBaseMillis()} for each of the independent listeners (i.e. stream status and followers)
     *
     * @param threadRate the maximum <i>rate</i> of api calls per second
     */
    default void setThreadRate(long threadRate) {
        this.setThreadDelay(1000 / threadRate);
    }

}
