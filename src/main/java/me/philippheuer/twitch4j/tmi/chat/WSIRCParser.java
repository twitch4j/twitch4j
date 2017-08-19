package me.philippheuer.twitch4j.tmi.chat;

import lombok.Getter;
import me.philippheuer.twitch4j.TwitchClient;

import java.util.*;


@Getter
public class WSIRCParser {
	/**
	 * Raw Message
	 */

	private Message message;
	private final TwitchClient twitchClient;
	private final IRCClient ircClient;

	/**
	 *
	 * @param client the Twitch Client
	 * @param ircClient IRC client (with WebSocket)
	 * @param message Raw message from WebSocket client
	 */
	WSIRCParser(TwitchClient client, IRCClient ircClient, String message) {
		this.twitchClient = client;
		this.ircClient = ircClient;
		this.message = parse(message);
	}

	/**
	 * parsing message to {@link Message}
	 * @param line Raw Message
	 * @return {@link Message}
	 */
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
	 * checking Ping received
	 * @return if ping received
	 */
	public boolean pingRecived() {
		return message.conatins("PING");
	}

	/**
	 * checking Pong received
	 * @return if pong received
	 */
	public boolean pongRecived() {
		return message.conatins("PONG");
	}

	/**
	 * Replaying message to the last sender when received message
	 * @param message message
	 */
	public void replay(String message) {
		if (this.message.getCommand().equalsIgnoreCase("PRIVMSG")) {
			ircClient.sendMessage(this.message.getParams().get(0).substring(1), message);
		} else if (this.message.getCommand().equalsIgnoreCase("WHISPER")) {
			ircClient.sendPrivateMessage(this.message.getParams().get(0), message);
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
