package me.philippheuer.twitch4j.auth.model.streamlabs;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.philippheuer.twitch4j.streamlabs.model.User;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class StreamlabsCredential {

	private String oAuthToken;

	private String oAuthRefreshToken;

	private final List<String> oAuthScopes = new ArrayList<String>();

	private User user;

	/**
	 * Constructor
	 */
	public StreamlabsCredential(String oAuthToken) {
		setOAuthToken(oAuthToken);
	}

	/**
	 *
	 * @return
	 */
	public void replaceCredential(StreamlabsCredential streamlabsCredential)
	{
		setOAuthToken(streamlabsCredential.getOAuthToken());
		setOAuthRefreshToken(streamlabsCredential.getOAuthRefreshToken());
		getOAuthScopes().clear();
		getOAuthScopes().addAll(streamlabsCredential.getOAuthScopes());
		setUser(streamlabsCredential.getUser());
	}
}
