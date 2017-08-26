package me.philippheuer.twitch4j.message.irc.events;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.message.irc.IRCParser;
import me.philippheuer.twitch4j.model.Channel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ChannelEvent {
	public enum Types {
		ALREADY_BANNED			("(?<user>.*) is already banned in this room."),
		ALREADY_EMOTE_ONLY_OFF	("This room is not in emote-only mode."),
		ALREADY_EMOTE_ONLY_ON	("This room is already in emote-only mode."),
		ALREADY_R9K_OFF			("This room is not in r9k mode."),
		ALREADY_R9K_ON			("This room is already in r9k mode."),
		ALREADY_SUBS_OFF		("This room is not in subscribers-only mode."),
		ALREADY_SUBS_ON			("This room is already in subscribers-only mode."),
		BAD_HOST_HOSTING		("This channel is hosting (?<channel>.*)."),
		BAD_UNBAN_NO_BAN		("(?<user>.*) is not banned from this room."),
		BAN_SUCCESS				("(?<user>.*) is banned from this room."),
		EMOTE_ONLY_OFF			("This room is no longer in emote-only mode."),
		EMOTE_ONLY_ON			("This room is now in emote-only mode."),
		HOST_OFF				("Exited host mode."),
		HOST_ON					("Now hosting (?<channel>.*)."),
		HOSTS_REMAINING			("There are (?<number>.*) host commands remaining this half hour."),
		MSG_CHANNEL_SUSPENDED	("This channel is suspended."),
		R9K_OFF					("This room is no longer in r9k mode."),
		R9K_ON					("This room is now in r9k mode."),
		SLOW_OFF				("This room is no longer in slow mode."),
		SLOW_ON					("This room is now in slow mode. You may send messages every (?<seconds>.*) seconds."),
		SUBS_OFF				("This room is no longer in subscribers-only mode."),
		SUBS_ON					("This room is now in subscribers-only mode."),
		TIMEOUT_SUCCESS			("(?<user>.*) has been timed out for (?<seconds>.*) seconds."),
		UNBAN_SUCCESS			("(?<user>.*) is no longer banned from this chat room."),
		UNRECOGNIZED_CMD		("Unrecognized command: (?<command>.*)");

		final Pattern pattern;

		Types(String regexPattern) {
			pattern = Pattern.compile(regexPattern);
		}

		Matcher getMatch(String message) {
			return pattern.matcher(message);
		}

		static Types getType(String name) {
			for(Types type : Types.values()) {
				if (type.name().equalsIgnoreCase(name)) return type;
			}
			return null;
		}
	}

	private final TwitchClient twitchClient;
	private final Types type;
	private final Matcher message;
	private final Channel channel;

	public ChannelEvent(TwitchClient client, IRCParser parser) {
		twitchClient = client;
		type = Types.getType((String) parser.getTags().getTag("msg-id"));
		message = type.getMatch(parser.getMessage());
		channel = client.getChannelEndpoint(parser.getChannelName()).getChannel();
		message.find();
	}

	public String getInteractedUser() {
		return message.group("user");
	}

	public String getHostedChannelName() {
		return message.group("channel");
	}

	public int getHostsLeft() {
		return Integer.parseInt(message.group("number"));
	}

	public int getTime() {
		return Integer.parseInt(message.group("seconds"));
	}

	public String getUsedCommand() {
		return message.group("command");
	}

	public String getMessage() {
		return message.group(0);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[")
				.append(type.name())
				.append("] ")
				.append(getMessage());
		return sb.toString();
	}
}
