package me.philippheuer.twitch4j.impl.api.helix;

import lombok.Getter;
import me.philippheuer.twitch4j.api.AbstractApiBinding;
import me.philippheuer.twitch4j.api.helix.HelixApi;
import me.philippheuer.twitch4j.api.helix.operations.WebHooksOperation;
import me.philippheuer.twitch4j.api.operations.*;
import me.philippheuer.twitch4j.api.operations.GamesOperation;
import me.philippheuer.twitch4j.api.operations.UsersOperation;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.NotAuthorizedException;
import org.springframework.util.MultiValueMap;

public class HelixTemplate extends AbstractApiBinding implements HelixApi {

    private UsersOperation usersOperation;
    private ChannelsOperation channelOperation;
    private GamesOperation gamesOperation;
    private VideosOperation videoOperation;
    private WebHooksOperation webHooksOperation;

    @Getter
    private final String apiUrl = "https://api.twitch.tv/helix/";

    public HelixTemplate(String clientId) {
        this(clientId, null);
    }

    public HelixTemplate(String clientId, String accessToken) {
        super(clientId, accessToken);
        initApi();
    }

    private void initApi() {

    }

	@Override
	public CredentialOperation credentialOperation() { return null; }

	@Override
    public UsersOperation userOperation() {
        return usersOperation;
    }

    @Override
    public ChannelsOperation channelOperation() {
        return channelOperation;
    }

	@Override
	public CommunitiesOperation communitiesOperation() { return null; }

	@Override
    public GamesOperation gamesOperation() { return gamesOperation; }

	@Override
	public IngestsOperation ingestOperation() { return null; }

	@Override
	public ClipsOperation clipsOperation() { return null; }

	@Override
    public VideosOperation videoOperation() { return videoOperation; }

	@Override
	public TeamsOperation teamsOperation() { return null; }

	@Override
    public WebHooksOperation webHooksOperation() { return webHooksOperation; }

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
