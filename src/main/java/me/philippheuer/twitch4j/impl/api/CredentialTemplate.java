package me.philippheuer.twitch4j.impl.api;

import lombok.AllArgsConstructor;
import me.philippheuer.twitch4j.api.IApi;
import me.philippheuer.twitch4j.api.model.ICredential;
import me.philippheuer.twitch4j.api.operations.CredentialOperation;

@AllArgsConstructor
public class CredentialTemplate implements CredentialOperation {
	private final IApi api;

	@Override
	public ICredential getCredentials() {
		return null;
	}

	@Override
	public ICredential refreshToken() {
		return null;
	}
}
