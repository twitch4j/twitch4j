package me.philippheuer.twitch4j.events.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.model.Channel;
import me.philippheuer.twitch4j.model.Cheer;
import me.philippheuer.twitch4j.model.User;

/**
 * This event gets called an oauth token expires.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AuthTokenExpiredEvent extends Event {

	/**
	 * Event Credential
	 */
	private final OAuthCredential credential;

	/**
	 * Event Constructor
	 *
	 * @param credential The expired oauth credential.
	 */
	public AuthTokenExpiredEvent(OAuthCredential credential) {
		this.credential = credential;
	}

}
