package com.github.twitch4j.common.events.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventChannel {

    /**
     * Channel Id
     */
    private final String id;

    /**
     * Channel Name
     */
    private final String name;

}
