package twitch4j.irc.chat;

import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import twitch4j.irc.api.UserChat;
import twitch4j.irc.TwitchMessageInterface;
import twitch4j.irc.exceptions.ChannelNotFoundException;
import twitch4j.irc.exceptions.UserNotFoundException;

public class ChannelCache {
	@Getter
	private final Set<IChannel> channels = new LinkedHashSet<>();
	private final SetMultimap<IChannel, IChannelUser> channelUsers = ImmutableSetMultimap.of();
	private final SetMultimap<IChannelUser, IChannel> userChannels = ImmutableSetMultimap.of();

	private final TwitchMessageInterface tmi;

	private RateLimiter directMessageRateLimiter;

	public ChannelCache(TwitchMessageInterface tmi) {
		System.out.println(tmi.getConfiguration().getBotCredentials());
		this.tmi = tmi;
		this.directMessageRateLimiter = RateLimiter.directMessageLimit(this.tmi.getConfiguration().getBotCredentials().isKnownBot(),
				this.tmi.getConfiguration().getBotCredentials().isVerified());
	}

	public void joinChannel(String channelName) {
		sendRaw("JOIN #" + channelName);
		ChannelImpl channel = new ChannelImpl(tmi, channelName);
		this.tmi.getApi().getChatters(channelName.toLowerCase())
				.subscribe(result -> {
					channel.setRateLimiter(RateLimiter.channelLimit(result.getChatters()
							.getModerators().contains(tmi.getConfiguration().getBotCredentials().username()),
							tmi.getConfiguration().getBotCredentials().isKnownBot(),
							this.tmi.getConfiguration().getBotCredentials().isVerified()));
					if (!channelExist(channel)) {
						registerChannel(channel);
					}
				});
	}

	public void leaveChannel(String channelName) {
		IChannel channel = getChannel(channelName);
		if (channelExist(channel)) {
			channel.getAllUsers().forEach(user -> removeChannelUser(channel, user.getName()));
			sendRaw("PART #" + channelName);
			channels.remove(channel);
		}

	}

	public IChannelUser createChannelUser(IChannel channel, String username) {
		ChannelUserImpl user = new ChannelUserImpl((ChannelImpl) channel, username, directMessageRateLimiter);
		registerUser(channel, user);
		return user;
	}

	public void removeChannelUser(IChannel channel, String username) {
		channel.getAllUsers().stream().filter(user -> user.getName().equalsIgnoreCase(username))
				.findFirst().ifPresent(user -> {
			channelUsers.remove(channel, user);
			userChannels.remove(user, channel);
		});
	}

	public IChannel getBotChannel() {
		return getChannel(tmi.getConfiguration().getBotCredentials().username());
	}

	public IUser createPrivateMessage(String username) {
		return new ChannelUserImpl((ChannelImpl) getBotChannel(), username, directMessageRateLimiter);
	}

	public void registerChannel(IChannel channel) {
		if (!channelExist(channel)) {
			channels.add(channel);
		}
	}

	public void registerUser(IChannel channel, IChannelUser user) {
		if (!channelUserExist(channel, user)) {
			channelUsers.put(channel, user);
			userChannels.put(user, channel);
		}
	}

	// TODO
	private boolean channelUserExist(IChannel channel, IChannelUser user) {
		return channelUsers.containsEntry(channel, user)
				&& userChannels.containsEntry(user, channel);
	}

	public IChannel getChannel(String name) throws ChannelNotFoundException {
		return channels.stream()
				.filter(channel -> channel.getName().equalsIgnoreCase(name))
				.findFirst().orElseThrow(() -> new ChannelNotFoundException(name));
	}

	public IUser getUser(String username) throws UserNotFoundException {
		return channelUsers.values()
				.stream().filter(user -> user.getName().equalsIgnoreCase(username))
				.map(user -> (IUser) user)
				.findFirst().orElseThrow(() -> new UserNotFoundException(username));
	}

	public List<IChannelUser> listAllUsers(IChannel channel) {
		return new ArrayList<>(channelUsers.get(channel));
	}

	public void purge() {
		channelUsers.clear();
		userChannels.clear();
		channels.clear();
	}

	protected void sendRaw(String rawMessage) {
		this.tmi.send(rawMessage);
	}

	private boolean channelExist(IChannel channel) {
		return channels.stream()
				.anyMatch(ch -> ch.getName().equalsIgnoreCase(channel.getName()));
	}
}
