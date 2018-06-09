package twitch4j.common.auth.storage;

import java.util.Collection;
import java.util.Optional;
import twitch4j.common.auth.ICredential;

public interface AuthStorage {
	Collection<ICredential> fetchAll();

	ICredential get(long userId);

	Optional<ICredential> get(String username);

	boolean register(ICredential credential);

	boolean unregister(ICredential credential);

	boolean update(ICredential credential);
}
