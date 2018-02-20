package me.philippheuer.twitch4j.api.helix.operations;

import me.philippheuer.twitch4j.api.helix.models.HelixStream;
import me.philippheuer.twitch4j.auth.ICredential;

public interface StreamsHelixOperation extends HelixOperation<HelixStream, Long> {
	@Override
	HelixStream get(ICredential credential);
}
