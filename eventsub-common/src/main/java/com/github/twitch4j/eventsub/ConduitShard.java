package com.github.twitch4j.eventsub;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import lombok.extern.jackson.Jacksonized;

@With
@Data
@Setter(AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConduitShard {

    /**
     * Shard ID.
     */
    @JsonProperty("id")
    private String shardId;

    /**
     * The shard status.
     * <p>
     * The subscriber receives events only for enabled shards.
     */
    private ShardStatus status;

    /**
     * The transport details used to send the notifications.
     */
    private EventSubTransport transport;

}
