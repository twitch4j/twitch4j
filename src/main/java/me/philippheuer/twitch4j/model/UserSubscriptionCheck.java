package me.philippheuer.twitch4j.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * Model representing the subscription status of a user.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
public class UserSubscriptionCheck {

	@JsonProperty("_id")
	private String id;

	private Channel channel;

	private Date createdAt;

}
