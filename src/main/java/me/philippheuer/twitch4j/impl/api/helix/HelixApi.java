package me.philippheuer.twitch4j.impl.api.helix;

import me.philippheuer.twitch4j.IClient;
import me.philippheuer.twitch4j.TwitchAPI;
import me.philippheuer.twitch4j.api.helix.IHelix;
import me.philippheuer.twitch4j.api.helix.operations.*;
import me.philippheuer.twitch4j.auth.ICredential;
import me.philippheuer.twitch4j.impl.api.Api;

public class HelixApi extends Api implements IHelix {

	public HelixApi(IClient client) {
		super(client, TwitchAPI.HELIX, IHelix.PREFIX_AUTHORIZATION);
	}

	@Override
	public ClipsHelixOperation clipsOperation() {
		return createService(ClipsHelixOperation.class);
	}

	@Override
	public GamesHelixOperation gamesOperation() {
		return createService(GamesHelixOperation.class);
	}

	@Override
	public StreamsHelixOperation streamsOperation() {
		return createService(StreamsHelixOperation.class);
	}

	@Override
	public UserHelixOperation userOperation() {
		return createService(UserHelixOperation.class);
	}

	@Override
	public VideosHelixOperation videosOperation() {
		return createService(VideosHelixOperation.class);
	}

	@Override
	public ICredential fetchUserInfo(ICredential credential) {
		return getClient().getKrakenApi().fetchUserInfo(credential);
	}
}
