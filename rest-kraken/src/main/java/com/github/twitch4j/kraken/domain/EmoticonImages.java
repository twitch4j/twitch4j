package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class EmoticonImages {
    private Integer emoticonSet;
    private Integer height;
    private Integer width;
    private String url;
}
