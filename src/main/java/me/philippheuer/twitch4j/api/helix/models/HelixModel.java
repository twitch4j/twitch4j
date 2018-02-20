package me.philippheuer.twitch4j.api.helix.models;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.api.kraken.operations.KrakenOperation;
import me.philippheuer.twitch4j.auth.ICredential;

import java.util.Optional;

@Setter
@Getter
public class HelixModel {
	private IClient client;
	private Optional<ICredential> credential = Optional.empty();

	protected <T, ID> T fetchUserOperation(KrakenOperation<T, ID> operation, ID id) {
		return (credential.isPresent()) ? operation.get(credential.get()) : operation.getById(id);
	}
}
