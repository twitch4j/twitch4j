package me.philippheuer.twitch4j.auth.twitch.model;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import me.philippheuer.twitch4j.model.User;

@Getter
@Setter
@NoArgsConstructor
public class TwitchCredential {

	String oAuthToken;

	List<String> oAuthScopes = new ArrayList<String>();

	User user;

	/**
	 * Constructor
	 */
	public TwitchCredential(String userName, String oAuthToken) {
		setOAuthToken(oAuthToken);
	}
}
