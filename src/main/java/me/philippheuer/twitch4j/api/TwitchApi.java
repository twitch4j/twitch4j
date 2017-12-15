package me.philippheuer.twitch4j.api;

import me.philippheuer.twitch4j.api.operations.*;
import org.springframework.social.ApiBinding;

public interface TwitchApi extends IApi, ApiBinding {
	CredentialOperation credentialOperation();
	UsersOperation userOperation();
	ChannelsOperation channelOperation();
	CommunitiesOperation communitiesOperation();
	GamesOperation gamesOperation();
	IngestsOperation ingestOperation();
	ClipsOperation clipsOperation();
	VideosOperation videoOperation();
	TeamsOperation teamsOperation();
}
