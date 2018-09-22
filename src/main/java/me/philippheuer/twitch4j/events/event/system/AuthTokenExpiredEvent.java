package me.philippheuer.twitch4j.events.event.system;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.auth.model.OAuthCredential;
import me.philippheuer.twitch4j.events.event.TwitchEvent;

/**
 * This event gets called an oauth token expires.
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class AuthTokenExpiredEvent extends TwitchEvent {

	/**
	 * Event Credential
	 */
	private OAuthCredential credential;

	/**
	 * Event Constructor
	 *
	 * @param credential The expired oauth credential.
	 */
	public AuthTokenExpiredEvent(OAuthCredential credential) {
		this.credential = credential;
	}

}
