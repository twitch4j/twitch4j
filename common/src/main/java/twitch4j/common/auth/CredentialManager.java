package twitch4j.common.auth;

import java.util.Arrays;
import java.util.Collection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import twitch4j.common.auth.storage.AuthStorage;

@Getter
@RequiredArgsConstructor
public class CredentialManager {
	private final AuthService service;
	private final AuthStorage storage;

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
