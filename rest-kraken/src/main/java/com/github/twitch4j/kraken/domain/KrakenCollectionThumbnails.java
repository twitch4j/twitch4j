package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class KrakenCollectionThumbnails {
    public String large;
    public String medium;
    public String small;
    public String template;
}
