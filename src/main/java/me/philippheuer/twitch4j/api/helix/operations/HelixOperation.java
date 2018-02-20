package me.philippheuer.twitch4j.api.helix.operations;

import me.philippheuer.twitch4j.auth.ICredential;

import java.util.Optional;

public interface HelixOperation<T, ID> {
	T get(ICredential credential);
	T getById(ID id);
	Optional<T> getByName(String name);
}
