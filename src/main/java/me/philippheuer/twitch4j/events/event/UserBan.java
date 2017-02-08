package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.User;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserBan extends Event {

	/**
	 * Event Channel
	 */
	private final Channel channel;

	/**
	 * Event Target User
	 */
	private final User user;

	/**
	 * Reason for Punishment
	 */
	private final String reason;

	/**
	 * Constructor
	 *
	 * @param channel Channel
	 * @param user    Target User
	 * @param reason  Reason for Ban.
	 */
	public UserBan(Channel channel, User user, String reason) {
		super();
		this.channel = channel;
		this.user = user;
		this.reason = reason;
	}
}
