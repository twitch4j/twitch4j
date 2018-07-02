package twitch4j;

import java.util.Set;
import javax.annotation.Nullable;

import lombok.*;
import twitch4j.common.IBotCredential;
import twitch4j.common.auth.Scope;

@Data
@AllArgsConstructor
@Setter(AccessLevel.PACKAGE)
public class Configuration {
	private final String clientId;
	private final String clientSecret;
	private final String userAgent;
	private final String redirectUri;
	private final Set<Scope> defaultScopes;
	private final boolean forceVerify;

	@Nullable
	private IBotCredential botCredentials;
}
