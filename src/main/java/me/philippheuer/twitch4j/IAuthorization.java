package me.philippheuer.twitch4j;

import org.springframework.social.oauth2.OAuth2Version;

public interface IAuthorization {
	String getToken();
	OAuth2Version getOAuth2Version();
}
