package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommerceData {

    /**
     * The login name for the user making the purchase
     */
    private String userName;

    /**
     * The display name for the user making the purchase
     */
    private String displayName;

    /**
     * The name for the channel where the commerce event took place
     */
    private String channelName;

    /**
     * The id for the user making the purchase
     */
    private String userId;

    /**
     * The id for the channel where the commerce event took place
     */
    private String channelId;

    /**
     * RFC 3339 timestamp of when the commerce event occurred
     */
    private String time;

    /**
     * HTTP(S) URL for the image associated with the purchased item
     */
    private String itemImageUrl;

    /**
     * Description for the purchased item
     */
    private String itemDescription;

    /**
     * Whether this purchase supports the channel
     */
    private Boolean supportsChannel;

    /**
     * The message accompanying this purchase
     */
    private CommerceMessage purchaseMessage;

}
