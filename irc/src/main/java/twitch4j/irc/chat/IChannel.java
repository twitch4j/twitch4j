package twitch4j.irc.chat;

import java.util.List;
import twitch4j.irc.chat.moderations.IEditor;
import twitch4j.irc.chat.moderations.IModerator;
import twitch4j.irc.exceptions.ModerationException;

public interface IChannel {
	String getName();

	List<IChannelUser> getAllUsers();

	List<IChannelUser> getModerators();

	List<IChannelUser> getSubscribers();

	IUser getOwner();

	IUser getBot();

	void sendMessage(String message);

	default void sendActionMessage(String message) {
		sendMessage("/me " + message);
	}

	boolean canEdit();

	boolean canModerate();

	IModerator getModeration() throws ModerationException;

	IEditor getEditor() throws ModerationException;
}
