package com.github.twitch4j.modules;

import com.github.twitch4j.TwitchClient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class IModule {
	private final String name;
	private final String author;
	private final String version;
	public abstract void enable(TwitchClient client);
	public abstract void disable();
	// String getMinimumTwitch4JVersion(); TODO: version implementing?

	@Override
	public String toString() {
		return String.format("%s v[%s] by %s", name, version, author);
	}
}
