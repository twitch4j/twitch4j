package twitch4j.irc.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import twitch4j.irc.chat.message.HostMask;
import twitch4j.irc.chat.message.Message;
import twitch4j.irc.chat.message.MessageCommand;
import twitch4j.irc.chat.message.TagsMessage;

@Data
@EqualsAndHashCode(callSuper = false)
public class RawMessage extends AbstractTMIEvent {
	private final MessageCommand command;
	private final String parameters;
	private final String message;
	private final HostMask hostmask;
	private final TagsMessage tags;

	public RawMessage(Message ircMessage) {
		this.command = ircMessage.getCommand();
		this.parameters = ircMessage.getParameters();
		this.message = ircMessage.getMessage();
		this.hostmask = ircMessage.getHostmask();
		this.tags = ircMessage.getTags();
	}
}
