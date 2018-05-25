package twitch4j.irc.chat;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import twitch4j.common.auth.Scope;
import twitch4j.irc.TwitchMessageInterface;
import twitch4j.irc.chat.moderations.EditorImpl;
import twitch4j.irc.chat.moderations.IEditor;
import twitch4j.irc.chat.moderations.IModerator;
import twitch4j.irc.chat.moderations.ModerationImpl;
import twitch4j.irc.exceptions.ModerationException;

@Data
public class ChannelImpl implements IChannel {
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	protected final TwitchMessageInterface tmi;
	private final String name;
	private RateLimiter rateLimiter;

	@Override
	public List<IChannelUser> getAllUsers() {
		return tmi.getCache().listAllUsers(this);
	}

	@Override
	public List<IChannelUser> getModerators() {
		return getAllUsers().stream()
				.filter(IChannelUser::hasModerator)
				.collect(Collectors.toList());
	}

	@Override
	public List<IChannelUser> getSubscribers() {
		return getAllUsers().stream()
				.filter(IChannelUser::isSubscriber)
				.collect(Collectors.toList());
	}

	@Override
	public IUser getOwner() {
		return tmi.getCache().getUser(getName());
	}

	@Override
	public IUser getBot() {
		return tmi.getCache().getBotChannel().getOwner();
	}

	@Override
	public void sendMessage(String message) {
		tmi.getCache().sendRaw(String.format("PRIVMSG #%s :%s", getName(), message));
	}

	@Override
	public boolean canEdit() {
		return false;
//		return tmi.getConfiguration().getBotCredentials().scopes().contains(Scope.CHANNEL_EDITOR)
//				&& tmi.getKraken()
//				.channelEndpoint()
//				.getEditors(tmi.getConfiguration().getBotCredentials())
//				.stream().anyMatch(user -> user.getName().equalsIgnoreCase(tmi.getConfiguration().getBotCredentials().username()));
	}

	@Override
	public boolean canModerate() {
		return getModerators().stream().anyMatch(user -> user.getName()
				.equalsIgnoreCase(tmi.getConfiguration()
						.getBotCredentials().username()) && user.hasModerator());
	}

	@Override
	public IModerator getModeration() throws ModerationException {
		if (!canModerate()) {
			throw new ModerationException(String.format("Cannot moderate this channel: \"%s\" - bot is not moderator", name));
		}

		return new ModerationImpl(this);
	}

	@Override
	public IEditor getEditor() throws ModerationException {
		if (!canEdit()) {
			throw new ModerationException(String.format("Cannot edit this channel: \"%s\" - bot is not editor or current access cannot be allowed to edit this channel.", name));
		}

		return new EditorImpl(this);
	}
}
