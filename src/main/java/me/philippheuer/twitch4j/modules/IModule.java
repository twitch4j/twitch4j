package me.philippheuer.twitch4j.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;

@Getter
@AllArgsConstructor
public abstract class IModule {
	private final String name;
	private final String author;
	private final String version;
	public abstract boolean enable(TwitchClient client);
	public abstract void disable();
	// String getMinimumTwitch4JVersion(); TODO: version implementing?
}
