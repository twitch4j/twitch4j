package twitch4j.common.auth;

import lombok.Getter;
import reactor.core.publisher.Mono;
import twitch4j.Configuration;
import twitch4j.common.auth.storage.AuthStorage;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class CredentialManager {
	private final AuthService service;
	private final AuthStorage storage;

	public CredentialManager(Configuration configuration, AuthStorage storage) {
		this.service = new AuthService(configuration);
		this.storage = storage;
	}

	public Mono<ICredential> authorize(String authorizationCode) {
		return service.create(authorizationCode)
				.doOnSuccess(storage::register);
	}

	public Mono<ICredential> refreshCredentials(ICredential credential) {
		return service.refresh(credential)
				.doOnSuccess(storage::update);
	}

	public void registerCredentials(Collection<ICredential.Builder> credentials) {
		credentials.forEach(this::registerCredential);
	}
	public void registerCredentials(ICredential.Builder... credentials) {
		registerCredentials(Arrays.asList(credentials));
	}
	public void registerCredential(ICredential.Builder builder) {
		storage.register(builder.build(service));
	}

	public void unregisterCredential(ICredential credential, boolean revoke) {
		if (storage.unregister(credential) && revoke) {
			service.revoke(credential).then().block();
		}
	}
}
