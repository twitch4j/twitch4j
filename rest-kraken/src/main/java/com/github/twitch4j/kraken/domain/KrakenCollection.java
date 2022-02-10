package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * @deprecated Due endpoints is deprecated decommission have been planned on <b>Febuary 28, 2022</b>.
 *             More details about decommission finds <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>
 */
@Data
@Deprecated
@Setter(AccessLevel.PRIVATE)
public class KrakenCollection {

    @JsonProperty("_id")
    private String id;

    private List<KrakenCollectionItem> items;

}
