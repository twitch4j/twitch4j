package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

/**
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Data
@Deprecated
public class KrakenSubscription {

	@JsonProperty("_id")
	private String id;

	private Instant createdAt;

	private KrakenUser user;

	@JsonProperty("sub_plan")
	private String subscriptionPlan;

	@JsonProperty("sub_plan_name")
	private String subscriptionName;

}
