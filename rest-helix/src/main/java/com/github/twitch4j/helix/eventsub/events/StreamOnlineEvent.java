package com.github.twitch4j.helix.eventsub.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.twitch4j.helix.eventsub.domain.StreamType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamOnlineEvent extends EventSubChannelEvent {

    /**
     * The event id.
     */
    private String id;

    /**
     * The stream type.
     */
    private StreamType type;

}
