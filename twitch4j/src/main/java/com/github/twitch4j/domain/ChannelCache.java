package com.github.twitch4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Channel Cache
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCache {

    /**
     * User Name
     */
    @With
    private String userName;

    /**
     * IsLive
     */
    private Boolean isLive;

    /**
     * Stream Title
     */
    private String title;

    /**
     * Current Game Id
     */
    private String gameId;

    /**
     * Last Follow Check
     */
    private Instant lastFollowCheck;

    /**
     * Total Follow Count
     */
    private final AtomicReference<Integer> followers = new AtomicReference<>();

}
