package me.philippheuer.twitch4j.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.enums.Scope;

@Data
@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OAuthRequest {

	private final Set<Scope> oAuthScopes = new HashSet<>();
	private String tokenId;
	private String type;

	/**
	 * Constructor
	 */
	public OAuthRequest() {
		// Generate random token
		setTokenId(UUID.randomUUID().toString().replace("-", ""));
	}
}
