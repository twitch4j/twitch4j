package me.philippheuer.twitch4j.auth.model;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import me.philippheuer.twitch4j.model.User;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthCredential {

	private String oAuthToken;

	private String oAuthRefreshToken;

	private final List<String> oAuthScopes = new ArrayList<String>();

	private Long userId;

	private String userName;

	private String displayName;

	/**
	 * Constructor
	 */
	public OAuthCredential(String oAuthToken) {
		setOAuthToken(oAuthToken);
	}

}
