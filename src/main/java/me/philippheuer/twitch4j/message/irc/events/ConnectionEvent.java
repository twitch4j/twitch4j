package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.enums.Endpoints;

@Getter
public class ConnectionEvent {

	private final String hostname;
	private final int port;
	@Setter
	private String reason;

	public ConnectionEvent() {
		String[] host = Endpoints.IRC.getURL().replace("wss://", "")
				.replace("/", "")
				.split(":");
		this.hostname = host[0];
		this.port = Integer.parseInt(host[1]);
	}


}
