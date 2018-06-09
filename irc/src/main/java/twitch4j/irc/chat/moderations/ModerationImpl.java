package twitch4j.irc.chat.moderations;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import twitch4j.irc.chat.IChannel;

@RequiredArgsConstructor
public class ModerationImpl implements IModerator {
	private final IChannel channel;

	@Override
	public void timeout(String user, Duration duration, String reason) {
		StringBuilder sb = new StringBuilder()
				.append(duration.getSeconds());
		if (reason != null) {
			sb.append(" ").append(reason);
		}
		channel.sendMessage(String.format("/timeout %s %s", user, sb.toString()));
	}

	@Override
	public void ban(String user, String reason) {
		StringBuilder sb = new StringBuilder(user);
		if (reason != null) {
			sb.append(" ").append(reason);
		}
		channel.sendMessage(String.format("/timeout %s", sb.toString()));
	}

	@Override
	public void unban(String user) {
		channel.sendMessage(String.format("/unban %s", user));
	}

	@Override
	public void emoteOnly() {
		channel.sendMessage("/emoteonly");
	}

	@Override
	public void emoteOnlyOff() {
		channel.sendMessage("/emoteonlyoff");
	}

	@Override
	public void r9k() {
		channel.sendMessage("/r9kbeta");
	}

	@Override
	public void r9kOff() {
		channel.sendMessage("/r9kbetaoff");
	}

	@Override
	public void slowmode(Duration slowDuration) {
		channel.sendMessage(String.format("/slow %d", slowDuration.getSeconds()));
	}

	@Override
	public void slowmodeOff() {
		channel.sendMessage("/slowoff");
	}

	@Override
	public void clearChat() {
		channel.sendMessage("/clear");
	}

	@Override
	public void followersOnly(Duration followDuration) {
		channel.sendMessage(String.format("/followers %d seconds", followDuration.getSeconds()));
	}

	@Override
	public void followersOff() {
		channel.sendMessage("/followersoff");
	}
}
