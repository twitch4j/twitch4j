package com.github.twitch4j.pubsub.domain;

import lombok.Data;
import lombok.Setter;

@Data
@Setter(onMethod_ = { @Deprecated })
public class BitsBadgeData {

    /**
     * The id for the user that earned a new badge
     */
    private String userId;

    /**
     * The login name for the user that earned a new badge
     */
    private String userName;

    /**
     * The id for the channel in which the badge was earned
     */
    private String channelId;

    /**
     * The login name for the channel in which the badge was earned
     */
    private String channelName;

    /**
     * The number of bits associated with the new badge
     */
    private Integer badgeTier;

    /**
     * The accompanying message when the badge was shared
     */
    private String chatMessage;

    /**
     * RFC 3339 timestamp of when the bits badge was awarded
     */
    private String time;

}
