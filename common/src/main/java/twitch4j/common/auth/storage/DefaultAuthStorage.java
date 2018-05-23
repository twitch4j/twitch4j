package twitch4j.common.auth.storage;

import twitch4j.common.auth.ICredential;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultAuthStorage implements AuthStorage {
	private final Map<Long, ICredential> credentials = new LinkedHashMap<>();

	@Override
	public Collection<ICredential> fetchAll() {
		return credentials.values();
	}

	@Override
	public ICredential get(long userId) {
		return credentials.get(userId);
	}

	@Override
	public Optional<ICredential> get(String username) {
		return fetchAll().stream().filter(credential -> credential.username().equals(username)).findFirst();
	}

	@Override
	public boolean register(ICredential credential) {
		return credentials.put(credential.userId(), credential) != null;
	}

	@Override
	public boolean unregister(ICredential credential) {
		return credentials.remove(credential.userId()) != null;
	}

	@Override
	public boolean update(ICredential credential) {
		return unregister(credential) && register(credential);
	}
}
