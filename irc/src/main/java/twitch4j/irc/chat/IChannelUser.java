package twitch4j.irc.chat;

public interface IChannelUser extends IUser {
	void sendMention(String message);

	IChannel getAssignedChannel();

	boolean hasModerator();

	boolean isSubscriber();

	boolean hasTurbo();

	boolean isOwner();

	boolean isBot();
}
