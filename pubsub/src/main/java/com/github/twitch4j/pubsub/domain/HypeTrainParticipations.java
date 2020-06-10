package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HypeTrainParticipations {
    @JsonProperty("BITS.CHEER")
    private Integer cheerBits;

    @JsonProperty("BITS.EXTENSION")
    private Integer extensionBits;

    @JsonProperty("BITS.POLL")
    private Integer pollBits;

    @JsonProperty("SUBS.TIER_1_SUB")
    private Integer subscribedTier1;

    @JsonProperty("SUBS.TIER_2_SUB")
    private Integer subscribedTier2;

    @JsonProperty("SUBS.TIER_3_SUB")
    private Integer subscribedTier3;

    @JsonProperty("SUBS.TIER_1_GIFTED_SUB")
    private Integer giftedTier1;

    @JsonProperty("SUBS.TIER_2_GIFTED_SUB")
    private Integer giftedTier2;

    @JsonProperty("SUBS.TIER_3_GIFTED_SUB")
    private Integer giftedTier3;
}
