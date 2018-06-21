package twitch4j.irc;

import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import twitch4j.Configuration;
import twitch4j.irc.api.ChatterResult;
import twitch4j.irc.api.UserChat;
import twitch4j.stream.rest.request.Router;
import twitch4j.stream.rest.route.Route;

@RequiredArgsConstructor
public class MessageInterfaceAPI {
	private final Configuration configuration;

	private final Router krakenRoute = Configuration.buildRouter("https://api.twitch.tv/kraken", Collections.emptyMap());
	private final Router tmiRoute = Configuration.buildRouter("http://tmi.twitch.tv", Collections.emptyMap());

	public Mono<ChatterResult> getChatters(String channelName) {
		// Validate Arguments
		Objects.requireNonNull(channelName, "Please provide a Channel Name!");

		Route<ChatterResult> route = Route.get(String.format("/group/user/%s/chatters", channelName), ChatterResult.class);
		return route.newRequest()
				.exchange(tmiRoute);
	}

	public Mono<UserChat> getUserChat(Long userId) {
		// Validate Arguments
		Objects.requireNonNull(userId, "Please provide a User ID!");

		Route<UserChat> route = Route.get(String.format("/users/%s/chat", userId), UserChat.class);

		return route.newRequest()
				.header("Client-ID", configuration.getClientId())
				.exchange(krakenRoute);
	}
}
