package me.philippheuer.twitch4j.api.helix;

import me.philippheuer.twitch4j.api.TwitchApi;
import me.philippheuer.twitch4j.api.helix.operations.WebHooksOperation;

public interface HelixApi extends TwitchApi {
	WebHooksOperation webHooksOperation();
}
