package me.philippheuer.twitch4j.auth.twitch.model;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import me.philippheuer.twitch4j.model.User;

@Data
@Getter
@Setter
@NoArgsConstructor
public class TwitchCredential {

	private String oAuthToken;

	private final List<String> oAuthScopes = new ArrayList<String>();

	private User user;

	/**
	 * Constructor
	 */
	public TwitchCredential(String oAuthToken) {
		setOAuthToken(oAuthToken);
	}

	/**
	 *
	 * @param twitchCredential
	 * @return
	 */
	public void replaceTwitchCredential(TwitchCredential twitchCredential)
	{
		setOAuthToken(twitchCredential.getOAuthToken());
		getOAuthScopes().clear();
		getOAuthScopes().addAll(twitchCredential.getOAuthScopes());
		setUser(twitchCredential.getUser());
	}
}
