package me.philippheuer.twitch4j.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthCredential {

	private String type;

	private String token;

	private Calendar tokenExpiresAt;

	private String refreshToken;

	private final Set<String> oAuthScopes = new HashSet<String>();

	private Long userId;

	private String userName;

	private String displayName;

	/**
	 * Class Constructor
	 *
	 * @param token The OAuth Token for a user.
	 */
	public OAuthCredential(String token) {
		setToken(token);
	}

}
