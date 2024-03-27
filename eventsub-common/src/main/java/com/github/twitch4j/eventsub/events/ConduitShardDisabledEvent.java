package com.github.twitch4j.eventsub.events;

import com.github.twitch4j.eventsub.EventSubTransport;
import com.github.twitch4j.eventsub.ShardStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class ConduitShardDisabledEvent extends EventSubEvent {

    /**
     * The ID of the conduit.
     */
    private String conduitId;

    /**
     * The ID of the disabled shard.
     */
    private String shardId;

    /**
     * The new status of the transport.
     */
    private ShardStatus status;

    /**
     * The disabled transport.
     */
    private EventSubTransport transport;

}
