package com.github.twitch4j.kraken.domain;

import lombok.Data;

import java.util.List;

/**
 * @deprecated Due endpoints is deprecated decommission have been planned on <b>Febuary 28, 2022</b>.
 *             More details about decommission finds <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>
 */
@Data
@Deprecated
public class KrakenUserList {
	/**
	 * Data
	 */
	private List<KrakenUser> users;
}
