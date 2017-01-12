package de.philippheuer.twitch4j.auth;

import java.util.ArrayList;
import java.util.List;

import lombok.*;

@Getter
@Setter
public class TwitchCredential {

	String userName;
	
	String oAuthToken;
	
	List<String> oAuthScopes = new ArrayList<String>();
	
	/**
	 * Constructor
	 */
	public TwitchCredential(String userName, String oAuthToken) {
		setUserName(userName);
		setOAuthToken(oAuthToken);
	}
}
