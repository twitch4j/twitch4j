package io.twitch4j.auth;

import java.util.Collection;
import java.util.Optional;

public interface ICredentialStore {
    Collection<ICredential> listCredentials();

    void open() throws Exception;

    void close() throws Exception;

    void put(ICredential credential);

    void delete(ICredential credential);

    ICredential getById(long userId);

    Optional<ICredential> getByUsername(String username);
}
