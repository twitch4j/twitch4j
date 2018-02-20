package me.philippheuer.twitch4j.api.helix;

import me.philippheuer.twitch4j.api.IApi;
import me.philippheuer.twitch4j.api.helix.operations.*;
import me.philippheuer.twitch4j.auth.ICredential;

public interface IHelix extends IApi {
	String PREFIX_AUTHORIZATION = "Bearer";

	ClipsHelixOperation clipsOperation();
	GamesHelixOperation gamesOperation();
	StreamsHelixOperation streamsOperation();
	UserHelixOperation userOperation();
	VideosHelixOperation videosOperation();
	ICredential fetchUserInfo(ICredential credential);
}
