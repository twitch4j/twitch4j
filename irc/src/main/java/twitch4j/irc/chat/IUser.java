package twitch4j.irc.chat;

public interface IUser {
	String getName();

	void sendPrivateMessage(String message);
}
