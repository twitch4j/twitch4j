package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import me.philippheuer.twitch4j.enums.SubscriptionPlan;

import java.time.Instant;

/**
 * Model representing a subscription.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class Subscription {

	@JsonProperty("_id")
	private String id;

	private Instant createdAt;

	private User user;

	@JsonProperty("sub_plan")
	private SubscriptionPlan subscriptionPlan;

	@JsonProperty("sub_plan_name")
	private String subscriptionName;

	public void setSubPlanByCode(String subPlan) {
		this.subscriptionPlan = SubscriptionPlan.fromString(subPlan);
	}
}
