package twitch4j.irc.chat.message;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import twitch4j.irc.utils.MessageUtil;
import twitch4j.irc.utils.TagsUtil;

@Data
public class Message {
	private final MessageCommand command;
	private final String parameters;
	private final String message;
	private final HostMask hostmask;
	private final TagsMessage tags;

	public static Message of(String rawMessage) {
		if (rawMessage.startsWith("@")) {
			return parseTags(rawMessage);
		} else {
			return parseNonTags(rawMessage);
		}
	}

	@SuppressWarnings("unchecked")
	private static Message parseTags(String rawMessage) {
		TagsMessage tags = null;
		MessageCommand command = MessageCommand.UNKNOWN;
		String parameters = null;
		String message = null;
		HostMask hostmask = null;

		String[] parts = rawMessage.split(" ", 5);
		if (parts.length == 5) {
			if (parts[4].startsWith(":")) {
				message = parts[4].substring(1);
			}
		}
		if (parts.length >= 4) {
			parameters = parts[3];
			if (parts.length > 4 && !parts[4].startsWith(":")) {
				parameters += " " + parts[4];
			}
		}
		if (parts.length >= 3) {
			command = MessageUtil.parseCommand(parts[2]);
		}
		if (parts.length >= 2) {
			hostmask = new HostMask(parts[1]);
		}
		if (parts.length >= 1) {
			tags = new TagsMessage(parseTagMap(parts[0]));
		}

		return new Message(command, parameters, message, hostmask, tags);
	}

	private static Map<String, String> parseTagMap(String tags) {
		Map<String, String> tagMap = new LinkedHashMap<>();
		if (tags.startsWith("@")) {
			tags = tags.substring(1);
		}
		for (String segment : tags.split(";")) {
			String key = segment.split("=")[0];
			String value = (segment.split("=").length > 1) ? segment.split("=")[1] : "";
			if (TagsUtil.mightBeEscaped(key)) {
				value = TagsUtil.replaceEscapes(value);
			}

			tagMap.put(key, value);
		}

		return tagMap;
	}

	private static Message parseNonTags(String rawMessage) {
		MessageCommand command = MessageCommand.UNKNOWN;
		StringBuilder parameters = new StringBuilder();
		StringBuilder message = new StringBuilder();
		HostMask hostmask = null;

		boolean msg = false;
		String[] parts = rawMessage.split(" ");
		for (String part : parts) {
			if (part.startsWith(":") && parts[0].equalsIgnoreCase(part)) {
				hostmask = new HostMask(part);
			} else if (parts[1].equalsIgnoreCase(part)) {
				command = MessageUtil.parseCommand(part);
			} else if (part.startsWith(":")) {
				msg = true;
				message.append(part);
			} else if (msg) {
				message.append(" ").append(part);
			} else {
				if (parameters.length() > 0) {
					parameters.append(" ");
				}
				parameters.append(part);
			}
		}

		return new Message(command, parameters.toString(), message.toString(), hostmask, null);
	}

	@Override
	public String toString() {
		return MessageUtil.stringify(this);
	}
}
