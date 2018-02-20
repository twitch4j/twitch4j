package me.philippheuer.twitch4j.api.helix.operations;

import me.philippheuer.twitch4j.api.helix.models.HelixClip;
import me.philippheuer.twitch4j.auth.ICredential;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Optional;

public interface ClipsHelixOperation extends HelixOperation<HelixClip, String> {
	@Override
	default HelixClip get(ICredential credential) {
		throw new UnsupportedOperationException("Getting clip by credentials is unsupported via Helix!");
	}

	@Override
	@GET("/clips")
	HelixClip getById(@Query("id") String id);

	@Override
	default Optional<HelixClip> getByName(String name) {
		throw new UnsupportedOperationException("Getting clip by name is unsupported via Helix!");
	}
}
