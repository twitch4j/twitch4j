package twitch4j.irc.chat.message;

import lombok.Data;

@Data
public class HostMask {
	private final String nick;
	private final String login;
	private final String hostname;

	HostMask(String rawHostmask) {
		if (rawHostmask.contains(":")) {
			rawHostmask = rawHostmask.split(":")[1];
		}
		if (rawHostmask.contains("!") && rawHostmask.contains("@")) {
			String[] parts = rawHostmask.split("[!@]");
			if (parts.length >= 3) {
				this.nick = parts[0];
				this.login = parts[1];
				this.hostname = parts[2];
			} else {
				this.nick = rawHostmask;
				this.login = null;
				this.hostname = null;
			}
		} else {
			this.nick = rawHostmask;
			this.login = null;
			this.hostname = null;
		}
	}


	@Override
	public String toString() {
		return nick +
				":" + login +
				":" + hostname;
	}
}
