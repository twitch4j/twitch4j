package me.philippheuer.twitch4j.api.operations;

import me.philippheuer.twitch4j.api.model.ICredential;

public interface CredentialOperation {
	ICredential getCredentials();
	ICredential refreshToken();
}
