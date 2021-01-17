package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KrakenCollection {

    @JsonProperty("_id")
    private String id;

    private List<KrakenCollectionItem> items;

}
