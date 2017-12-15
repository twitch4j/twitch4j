package me.philippheuer.twitch4j.impl.api;

import me.philippheuer.twitch4j.api.AbstractApiBinding;
import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.api.operations.*;
import org.springframework.util.MultiValueMap;

public class TwitchTemplate extends AbstractApiBinding implements TwitchApi {

    private final String URL = "https://api.twitch.tv/kraken/";

    public TwitchTemplate(String clientId, String accessToken) {
        super(clientId, accessToken);
    }

	@Override
	public CredentialOperation credentialOperation() {
		return null;
	}

	@Override
	public UsersOperation userOperation() {
		return null;
	}

	@Override
	public ChannelsOperation channelOperation() {
		return null;
	}

	@Override
	public CommunitiesOperation communitiesOperation() {
		return null;
	}

	@Override
	public GamesOperation gamesOperation() {
		return null;
	}

	@Override
	public IngestsOperation ingestOperation() {
		return null;
	}

	@Override
	public ClipsOperation clipsOperation() {
		return null;
	}

	@Override
	public VideosOperation videoOperation() {
		return null;
	}

	@Override
	public TeamsOperation teamsOperation() {
		return null;
	}

	@Override
	public String getApiUrl() {
		return null;
	}

	@Override
	public <T> T get(String endpoint, Class<T> type, MultiValueMap<String, String> params) {
		return null;
	}

	@Override
	public <T> T post(String endpoint, Class<T> type, Object data, MultiValueMap<String, String> params) {
		return null;
	}

	@Override
	public void put(String endpoint, Object data, MultiValueMap<String, String> params) {

	}

	@Override
	public void delete(String endpoint, Object data, MultiValueMap<String, String> params) {

	}
}
