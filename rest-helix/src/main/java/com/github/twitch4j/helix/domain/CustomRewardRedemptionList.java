package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CustomRewardRedemptionList {

    @JsonProperty("data")
    private List<CustomRewardRedemption> redemptions;

    private HelixPagination pagination;

}
