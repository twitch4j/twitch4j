package com.github.twitch4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
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
     * Current Viewer Count
     */
    private final AtomicReference<Integer> viewerCount = new AtomicReference<>();

    /**
     * Last Follow Check
     */
    private Instant lastFollowCheck;

    /**
     * Total Follow Count
     */
    private final AtomicReference<Integer> followers = new AtomicReference<>();

    /**
     * Clip Query Started At
     */
    private final AtomicReference<Instant> clipWindowStart = new AtomicReference<>();

    /**
     * Construct Channel Cache
     *
     * @param userName the name of the channel.
     */
    public ChannelCache(String userName) {
        this.userName = userName;
    }

}
