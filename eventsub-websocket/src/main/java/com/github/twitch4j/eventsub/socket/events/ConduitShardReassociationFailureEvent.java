package com.github.twitch4j.eventsub.socket.events;

import com.github.twitch4j.eventsub.socket.TwitchEventSocket;
import com.github.twitch4j.eventsub.socket.conduit.TwitchConduitSocketPool;
import com.github.twitch4j.helix.domain.ShardsInput;
import lombok.Value;

/**
 * Fired when a websocket shard unexpected disconnected and could not be re-associated with the conduit after reconnecting.
 */
@Value
public class ConduitShardReassociationFailureEvent {

    /**
     * The websocket that reconnected after an unexpected connection loss,
     * but could not be re-associated as a conduit shard.
     */
    TwitchEventSocket socket;

    /**
     * The Conduit pool containing the disconnected conduit shard.
     */
    TwitchConduitSocketPool pool;

    /**
     * The ID of the shard that disconnected.
     */
    String shardId;

    /**
     * The exception thrown by {@link com.github.twitch4j.helix.TwitchHelix#updateConduitShards(String, ShardsInput)}
     * when trying to re-associate the websocket as a conduit shard for the given ID.
     */
    Exception exception;

}
