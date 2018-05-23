package twitch4j;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import twitch4j.common.auth.ICredential;
import twitch4j.common.auth.Scope;

import javax.annotation.Nullable;
import java.util.Set;

@Data
@Setter(AccessLevel.PACKAGE)
public class Configuration {
	@Nullable
	private ICredential botCredentials;
	private final String clientId;
	private final String clientSecret;
	private final String userAgent;
	private final String redirectUri;

	private final Set<Scope> defaultScopes;
	private final boolean forceVerify;
}
