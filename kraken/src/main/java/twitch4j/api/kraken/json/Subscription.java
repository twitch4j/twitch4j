package twitch4j.api.kraken.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import twitch4j.common.enums.SubscriptionPlan;

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
}
