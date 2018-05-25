package twitch4j.irc.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ChannelUserImpl implements IChannelUser {
	private final ChannelImpl assignedChannel;
	private final String name;

	private final RateLimiter dmRateLimit;

	private boolean moderator;
	private boolean subscriber;
	private boolean turbo;

	@Override
	public void sendMention(String message) {
		assignedChannel.sendMessage(String.format("@%s, %s", name, message));
	}

	@Override
	public boolean hasModerator() {
		return moderator;
	}

	@Override
	public boolean hasTurbo() {
		return turbo;
	}

	@Override
	public boolean isOwner() {
		return assignedChannel.getName().equalsIgnoreCase(name);
	}

	@Override
	public boolean isBot() {
		return assignedChannel.tmi.getConfiguration()
				.getBotCredentials().username().equalsIgnoreCase(name);
	}

	@Override
	public void sendPrivateMessage(String message) {
		if (!isBot()) {
			dmRateLimit.queue(() -> assignedChannel.sendMessage(String.format("/w %s %s", name, message)));
		} else throw new RuntimeException("Can't send private message to myself!");
	}
}
