package me.philippheuer.twitch4j.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.*;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.auth.model.TwitchCredential;
import me.philippheuer.twitch4j.model.Channel;

@Getter
@Setter
public class CredentialManager {

	/**
	 * Holds the API Instance
	 */
	private TwitchClient twitchClient;

	// IRC Key
	public static String CREDENTIAL_IRC = "IRC";

	private final Map<String, TwitchCredential> oAuthCredentials = new HashMap<>();

	/**
	 * Constructor
	 */
	public CredentialManager(TwitchClient twitchClient) {
		super();

		setTwitchClient(twitchClient);
	}

	/**
	 * Adds Twitch Credentials to the Credential Manager
	 * @param key Key
	 * @param twitchCredential Credential Instance
	 */
	public void addCredential(String key, TwitchCredential twitchCredential) {
		getOAuthCredentials().put(key, twitchCredential);
	}

	/**
	 * Get Twitch credentials for channel
	 * @param channel Channel
	 * @return Optional<TwitchCredential> credential with oauth token and acces scope.
	 */
	public Optional<TwitchCredential> getForChannel(Channel channel) {
		if(getOAuthCredentials().containsKey(channel.getId().toString())) {
			return Optional.ofNullable(getOAuthCredentials().get(channel.getId().toString()));
		} else {
			return Optional.empty();
		}
	}

	/**
	 * Get Twitch credentials for irc
	 * @return Optional<TwitchCredential> credential with oauth token and acces scope.
	 */
	public Optional<TwitchCredential> getForIRC() {
		return Optional.ofNullable(getOAuthCredentials().get(CREDENTIAL_IRC));
	}


	public OAuth getOAuth() {
		return new OAuth(getTwitchClient());
	}
}
