package me.philippheuer.twitch4j.authorize.repositories;

import me.philippheuer.twitch4j.api.model.ICredential;

import java.util.Optional;

public interface IRepository {
	void registerCredential(ICredential credential);
	void unregisterCredential(ICredential credential);
	void refreshCredential(ICredential credential);

	ICredential getCredentialById(long id);
	ICredential getCredentialByToken(String token);
	Optional<ICredential> getCredentialByUsername(String username);
}
