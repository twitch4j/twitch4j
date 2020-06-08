package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChannelBitsData {

    /**
     * The id for the user that cheered
     */
    private String userId;

    /**
     * The login name for the user that cheered
     */
    private String userName;

    /**
     * The id for the channel where the bits were used
     */
    private String channelId;

    /**
     * The login name of the channel where the bits were used
     */
    private String channelName;

    /**
     * RFC 3339 timestamp of when the bits were used
     */
    private String time;

    /**
     * The message sent with the cheer
     */
    private String chatMessage;

    /**
     * The number of bits used
     */
    private Integer bitsUsed;

    /**
     * A cumulative total of all bits this user has cheered in this channel
     */
    private Integer totalBitsUsed;

    /**
     * The event type for this bits usage (e.g. "cheer")
     */
    private String context;

    /**
     * If a non-anonymous user reached a new badge level with this cheer,
     * this object will contain more information regarding the new level
     */
    private BadgeEntitlement badgeEntitlement;

    @Data
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BadgeEntitlement {
        /**
         * The number of bits associated with the previous badge
         */
        private int previousVersion;

        /**
         * The number of bits associated with the new badge
         */
        private int newVersion;
    }

}
