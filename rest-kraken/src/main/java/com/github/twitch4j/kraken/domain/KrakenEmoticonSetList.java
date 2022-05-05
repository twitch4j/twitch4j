package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;
import java.util.Set;

/**
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Data
@Deprecated
@Setter(AccessLevel.PRIVATE)
public class KrakenEmoticonSetList {
    private Map<String, Set<KrakenEmoticon>> emoticonSets;
}
