package com.github.twitch4j.kraken.domain;

import lombok.Data;

import java.util.List;

@Data
public class KrakenSubscriptionList {

	private List<KrakenSubscription> subscriptions;

}
