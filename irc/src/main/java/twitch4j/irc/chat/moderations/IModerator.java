package twitch4j.irc.chat.moderations;

import java.time.Duration;

public interface IModerator {
	void timeout(String user, Duration duration, String reason);

	void ban(String user, String reason);

	void unban(String user);

	void emoteOnly();

	void emoteOnlyOff();

	void r9k();

	void r9kOff();

	void slowmode(Duration slowDuration);

	void slowmodeOff();

	void clearChat();

	void followersOnly(Duration followDuration);

	void followersOff();

	default void timeout(String user, Duration duration) {
		timeout(user, duration, null);
	}

	default void ban(String user) {
		ban(user, null);
	}
}
