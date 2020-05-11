package com.github.twitch4j.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Channel Cache
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
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
     * Last Follow Check
     */
    private LocalDateTime lastFollowCheck;
}
