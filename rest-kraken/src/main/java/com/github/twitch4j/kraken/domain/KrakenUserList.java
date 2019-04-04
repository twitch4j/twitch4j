package com.github.twitch4j.kraken.domain;

import java.util.List;

import lombok.Data;

@Data
public class KrakenUserList {
	/**
	 * Data
	 */
	private List<KrakenUser> users;
}
