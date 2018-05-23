package twitch4j.common.auth;

import lombok.*;
import lombok.experimental.Accessors;
import twitch4j.common.auth.storage.AuthStorage;
import twitch4j.common.auth.storage.DefaultAuthStorage;

import java.util.*;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Manager {
	private final AuthConfig config;
	private final AuthService service = new AuthService(this);
	private final AuthStorage storage;

	public static Builder builder() {
		return new Builder();
	}

	public void registerCredentials(Collection<ICredential.Builder> credentials) {
		credentials.forEach(this::registerCredential);
	}

	private void registerCredential(ICredential.Builder builder) {
		builder.build(service);
	}

	public Optional<ICredential> getBotCredential() {
		return storage.fetchAll().stream().filter(ICredential::chatBot).findFirst();
	}

	@Data
	@Accessors(fluent = true, chain = true)
	public static class Builder {
		private String clientId;
		private String clientSecret;
		private String redirectUri = "http://localhost/";
		private boolean forceVerify = false;
		private AuthStorage storage = new DefaultAuthStorage();
		private final Set<Scope> defaultScopes = new LinkedHashSet<>();

		public Builder scope(Scope... scopes) {
			return scope(Arrays.asList(scopes));
		}

		public Builder scope(Collection<Scope> scopes) {
			defaultScopes.addAll(scopes);
			return this;
		}

		public Manager build() {
			Objects.requireNonNull(clientId, "client_id");
			Objects.requireNonNull(clientSecret, "client_secret");
			if (defaultScopes.isEmpty()) {
				throw new NullPointerException("default_scopes");
			}

			return new Manager(AuthConfig.of(this), storage);
		}
	}
}
