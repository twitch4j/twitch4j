package me.philippheuer.twitch4j.events.event.irc;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.message.irc.parsers.commands.Parse;

@Data
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class IRCMessageEvent<T extends Parse> extends Event {
	private final T parser;

	public IRCMessageEvent(T parser) {
		this.parser = parser;
	}
}
