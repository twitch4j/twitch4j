package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Emoticon {
    private Integer id;
    private String regex;
    private EmoticonImages images;
}
