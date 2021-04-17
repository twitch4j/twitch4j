package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

@Data
@Setter(AccessLevel.PRIVATE)
public class KrakenEmoticonSetList {
    private Map<String, Set<KrakenEmoticon>> emoticonSets;
}
