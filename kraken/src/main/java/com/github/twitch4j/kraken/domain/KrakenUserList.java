package com.github.twitch4j.kraken.domain;

import lombok.Data;

import java.util.List;

@Data
public class KrakenUserList {
	/**
	 * Data
	 */
	private List<KrakenUser> users;
}
