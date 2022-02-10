package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @deprecated Due endpoints is deprecated decommission have been planned on <b>Febuary 28, 2022</b>.
 *             More details about decommission finds <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>
 */
@Data
@Deprecated
@ToString(callSuper = true)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class KrakenFollowList extends AbstractResultList {
    private List<KrakenFollow> follows;
}
