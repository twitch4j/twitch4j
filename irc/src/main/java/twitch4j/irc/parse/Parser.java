package twitch4j.irc.parse;

import twitch4j.irc.chat.message.Message;
import twitch4j.irc.event.AbstractTMIEvent;

public interface Parser<T extends AbstractTMIEvent> {
	T parse(Message ircMessage);
}
