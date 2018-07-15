package me.philippheuer.twitch4j.model;

import java.util.List;
import lombok.Data;

/**
 * Model representing subscribers.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class SubscriptionList {
	/**
	 * List of Entity
	 */
	private List<Subscription> subscriptions;

}
