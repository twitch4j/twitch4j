package me.philippheuer.twitch4j.auth.model.twitch;

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

	private String oAuthRefreshToken;

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
	public void replaceCredential(TwitchCredential twitchCredential)
	{
		setOAuthToken(twitchCredential.getOAuthToken());
		setOAuthRefreshToken(twitchCredential.getOAuthRefreshToken());
		getOAuthScopes().clear();
		getOAuthScopes().addAll(twitchCredential.getOAuthScopes());
		setUser(twitchCredential.getUser());
	}
}
