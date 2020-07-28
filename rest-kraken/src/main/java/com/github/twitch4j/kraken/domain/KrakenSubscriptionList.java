package com.github.twitch4j.kraken.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class KrakenSubscriptionList extends AbstractResultList {

	private List<KrakenSubscription> subscriptions;

}
