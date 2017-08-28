package me.philippheuer.twitch4j.message.irc.events;

import lombok.AccessLevel;
import lombok.Getter;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.message.irc.IRCTags;

@Getter
public class RoomStateEvent {
	private final String channel;
	private final IRCTags tags;

	@Getter(AccessLevel.NONE)
	private final IRCParser parser;

	public RoomStateEvent(IRCParser parser) {
		this.parser = parser;
		this.channel = parser.getChannelName();
		this.tags = parser.getTags();
	}

	public Status getStatus(String tag_name) {
		return new Status(parser, tag_name);
	}

	@Getter(AccessLevel.PUBLIC)
	public class Status {
		private final String channel;
		private final int time;

		private final boolean enable;

		private Status(IRCParser parser, String tag_name) {
			this.channel = parser.getChannelName();
			this.time = (int) parser.getTags().getTag(tag_name);
			this.enable = this.time > -1;
		}
	}
}
