package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;

import java.util.*;

@Getter
public class IRCParser {

	private final TwitchClient twitchClient;
	private final Message message;


	public IRCParser(TwitchClient client, String message) {
		this.twitchClient = client;
		this.message = parse(message);
	}

	private Message parse(String line) {
		Message message = new Message();
		message.raw = line;
		int position = 0;
		int nextspace = 0;
		// parsing!
		if (line.charAt(0) == '@') {
			String[] rawTags;

			nextspace = line.indexOf(" ");
			System.out.println(nextspace);
			if (nextspace == -1) {
				return null;
			}

			rawTags = line.substring(1, nextspace).split(";");

			for (String tag : rawTags) {
				String[] pair = tag.split("=");

				if (pair.length == 2) {
					String[] subtags;
					if (pair[1].contains(",")) {
						Map<String, String> stagList = new HashMap<>();
						List<String> list = new ArrayList<String>();
						Arrays.asList(pair[1].split(",")).forEach(subtag -> {
							if (subtag.contains("/")) {
								String[] stagg = subtag.split("/");
								stagList.put(stagg[0], stagg[1]);
							} else {
								list.add(subtag);
							}
						});
						if (stagList.isEmpty() && !list.isEmpty()) {
							String[] stagListing = new String[list.size()];
							list.toArray(stagListing);
							message.tags.put(pair[0], stagListing);
						} else if (!stagList.isEmpty() && list.isEmpty()) {
							message.tags.put(pair[0], stagList);
						}
					} else {
						message.tags.put(pair[0], pair[1]);
					}
				} else {
					message.tags.put(pair[0], true);
				}
			}
			position = nextspace + 1;
		}

		while (line.charAt(position) == ' ') {
			position++;
		}

		if (line.charAt(position) == ':') {
			nextspace = line.indexOf(" ", position);
			if (nextspace == -1) {
				return null;
			}
			message.prefix = line.substring(position + 1, nextspace);
			position = nextspace + 1;

			while (line.charAt(position) == ' ') {
				position++;
			}
		}

		nextspace = line.indexOf(" ", position);

		if (nextspace == -1) {
			if (line.length() > position) {
				message.command = line.substring(position);
			}
			return message;
		}

		message.command = line.substring(position, nextspace);

		position = nextspace + 1;

		while (line.charAt(position) == ' ') {
			position++;
		}

		while (position < line.length()) {
			nextspace = line.indexOf(" ", position);

			if (line.charAt(position) == ':') {
				String param = line.substring(position + 1);
				message.params.add(param);
				break;
			}

			if (nextspace != -1) {
				String param = line.substring(position, nextspace);
				message.params.add(param);
				position = nextspace + 1;

				while (line.charAt(position) == ' ') {
					position++;
				}
				continue;
			}

			if (nextspace == -1) {
				String param = line.substring(position);
				message.params.add(param);
				break;
			}
		}

		return message;
	}

	/**
	 * Replaying message to the last sender where received message
	 * @param message message
	 */
	public void replay(String message) {
		if (this.message.getCommand().equalsIgnoreCase("PRIVMSG")) {
			twitchClient.getTMI().sendMessage(this.message.getParams().get(0).substring(1), message);
		} else if (this.message.getCommand().equalsIgnoreCase("WHISPER")) {
			twitchClient.getTMI().sendPrivateMessage(this.message.getParams().get(0), message);
		}
	}

	@Getter
	class Message {
		private HashMap<String, Object> tags = new HashMap<String, Object>();
		private String prefix;
		private String command;
		private ArrayList<String> params = new ArrayList<String>();
		private String raw;

		boolean conatins(CharSequence s) { return raw.contains(s); }
	}
}
