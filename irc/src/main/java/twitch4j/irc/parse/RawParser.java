package twitch4j.irc.parse;

import twitch4j.irc.chat.message.Message;
import twitch4j.irc.event.RawMessage;

public class RawParser implements Parser<RawMessage> {
	@Override
	public RawMessage parse(Message ircMessage) {
		return new RawMessage(ircMessage);
	}
}
