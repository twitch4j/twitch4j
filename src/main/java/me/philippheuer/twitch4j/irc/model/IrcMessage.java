/*
 * MIT License
 *
 * Copyright (c) 2018 twitch4j
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.philippheuer.twitch4j.irc.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * IRC Parser.
 * @see <a href = "https://github.com/3ventic/TwitchChatSharp/blob/master/TwitchChatSharp/IrcMessage.cs#L201">https://github.com/3ventic/TwitchChatSharp/blob/master/TwitchChatSharp/IrcMessage.cs#L201</a>
 * @author Werner [https://github.com/3ventic]
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@AllArgsConstructor
public class IrcMessage {
	/**
	 * IRC Command
	 */
	private final IrcCommand command;
	/**
	 * IRC Parameters
	 */
	private final String parameters;
	/**
	 * IRC Message
	 */
	private final String message;
	/**
	 * IRC Host Mask which contains username.
	 */
	private final String hostmask;
	/**
	 * IRC Tags V3
	 * @see TagsV3
	 */
	private final TagsV3 tags;

	private enum ParserState {
		STATE_NONE,
		STATE_V3,
		STATE_PREFIX,
		STATE_COMMAND,
		STATE_PARAM,
		STATE_TRAILING
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!tags.isEmpty()) {
			sb.append("@").append(tags).append(" ");
		}
		sb.append(hostmask).append(" ")
				.append((command.name().startsWith("RPL_")) ? command.name().substring(4) : command.name()).append(" ")
				.append(parameters);
		if (message != null) {
			sb.append(" :").append(message);
		}
		return sb.toString();
	}

	/**
	 * Parsing Raw Message
	 * @param rawMessage one line raw message
	 * @return Parsed message into {@link IrcMessage} class
	 */
	public static IrcMessage parseMessage(String rawMessage) {

		Map<String, String> tags = new HashMap<>();

		ParserState state = ParserState.STATE_NONE;
		int[] starts = {0, 0, 0, 0, 0, 0};
		int[] lens = {0, 0, 0, 0, 0, 0};
		for (int i = 0; i < rawMessage.length(); ++i) {
			lens[state.ordinal()] = i - starts[state.ordinal()] - 1;
			if (state.equals(ParserState.STATE_NONE) && rawMessage.startsWith("@")) {
				state = ParserState.STATE_V3;
				starts[state.ordinal()] = ++i;

				int start = i;
				String key = null;
				for (; i < rawMessage.length(); ++i) {
					if (rawMessage.toCharArray()[i] == '=') {
						key = rawMessage.substring(start, i - start);
						start = i + 1;
					} else if (rawMessage.toCharArray()[i] == ';') {
						if (key == null)
							tags.put(rawMessage.substring(start, i - start), "1");
						else
							tags.put(key, rawMessage.substring(start, i - start));
						start = i + 1;
					} else if (rawMessage.toCharArray()[i] == ' ') {
						if (key == null)
							tags.put(rawMessage.substring(start, i - start), "1");
						else
							tags.put(key, rawMessage.substring(start, i - start));
						break;
					}
				}
			} else if (state.ordinal() < ParserState.STATE_PREFIX.ordinal() && rawMessage.toCharArray()[i] == ':') {
				state = ParserState.STATE_PREFIX;
				starts[state.ordinal()] = ++i;
			} else if (state.ordinal() < ParserState.STATE_COMMAND.ordinal()) {
				state = ParserState.STATE_COMMAND;
				starts[state.ordinal()] = ++i;
			} else if (state.ordinal() < ParserState.STATE_TRAILING.ordinal() && rawMessage.toCharArray()[i] == ':') {
				state = ParserState.STATE_TRAILING;
				starts[state.ordinal()] = ++i;
				break;
			} else if (state.equals(ParserState.STATE_COMMAND)) {
				state = ParserState.STATE_PARAM;
				starts[state.ordinal()] = i;
			}
			while (i < rawMessage.length() && rawMessage.toCharArray()[i] != ' ') {
				++i;
			}
		}

		lens[state.ordinal()] = rawMessage.length() - starts[state.ordinal()];
		String cmd = rawMessage.substring(starts[ParserState.STATE_COMMAND.ordinal()], lens[ParserState.STATE_COMMAND.ordinal()]);

		IrcCommand command = IrcCommand.UNKNOWN;
		switch (cmd) {
			case "PRIVMSG":
				command = IrcCommand.PRIV_MSG;
				break;
			case "NOTICE":
				command = IrcCommand.NOTICE;
				break;
			case "PING":
				command = IrcCommand.PING;
				break;
			case "PONG":
				command = IrcCommand.PONG;
				break;
			case "HOSTTARGET":
				command = IrcCommand.HOST_TARGET;
				break;
			case "CLEARCHAT":
				command = IrcCommand.CLEAR_CHAT;
				break;
			case "USERSTATE":
				command = IrcCommand.USER_STATE;
				break;
			case "GLOBALUSERSTATE":
				command = IrcCommand.GLOBAL_USER_STATE;
				break;
			case "NICK":
				command = IrcCommand.NICK;
				break;
			case "JOIN":
				command = IrcCommand.JOIN;
				break;
			case "PART":
				command = IrcCommand.PART;
				break;
			case "PASS":
				command = IrcCommand.PASS;
				break;
			case "CAP":
				command = IrcCommand.CAP;
				break;
			case "001":
				command = IrcCommand.RPL_001;
				break;
			case "002":
				command = IrcCommand.RPL_002;
				break;
			case "003":
				command = IrcCommand.RPL_003;
				break;
			case "004":
				command = IrcCommand.RPL_004;
				break;
			case "353":
				command = IrcCommand.RPL_353;
				break;
			case "366":
				command = IrcCommand.RPL_366;
				break;
			case "372":
				command = IrcCommand.RPL_372;
				break;
			case "375":
				command = IrcCommand.RPL_375;
				break;
			case "376":
				command = IrcCommand.RPL_376;
				break;
			case "WHISPER":
				command = IrcCommand.WHISPER;
				break;
			case "SERVERCHANGE":
				command = IrcCommand.SERVER_CHANGE;
				break;
			case "RECONNECT":
				command = IrcCommand.RECONNECT;
				break;
			case "ROOMSTATE":
				command = IrcCommand.ROOM_STATE;
				break;
			case "USERNOTICE":
				command = IrcCommand.USER_NOTICE;
				break;
		}

		String parameters = rawMessage.substring(starts[ParserState.STATE_PARAM.ordinal()], lens[ParserState.STATE_PARAM.ordinal()]);
		String message = rawMessage.substring(starts[ParserState.STATE_TRAILING.ordinal()], lens[ParserState.STATE_TRAILING.ordinal()]);
		String hostmask = rawMessage.substring(starts[ParserState.STATE_PREFIX.ordinal()], lens[ParserState.STATE_PREFIX.ordinal()]);
		return new IrcMessage(command, parameters, message, hostmask, parseIrcTags(tags));
	}

	private static TagsV3 parseIrcTags(Map<String, String> tags) {
		if (tags.isEmpty())
			return null;

		Map<String, Object> tagsParsed = new HashMap<>();

		tags.forEach((key, value) -> {
			if (key.equalsIgnoreCase("badges")) {
				Map<String, Integer> badges = new HashMap<>();
				Arrays.asList(value.split(",")).forEach(badge ->
						badges.put(badge.split("/")[0], Integer.parseInt(badge.split("/")[1])));
				if (badges.size() > 0) {
					tagsParsed.put(key, Collections.unmodifiableMap(badges));
				}
			} else if (key.equalsIgnoreCase("emotes")) {
				Map<Integer, List<Integer[]>> emotes = new HashMap<Integer, List<Integer[]>>();
				Arrays.asList(value.split("/")).forEach(emote -> {
					int k = Integer.parseInt(emote.split(":")[0]);
					List<Integer[]> v = Arrays.stream(emote.split(":")[1].split(","))
							.map(val -> {
								String[] v2 = val.split("-");
								return new Integer [] {Integer.parseInt(v2[0]), Integer.parseInt(v2[1])};
							}).collect(Collectors.toList());
					emotes.put(k, Collections.unmodifiableList(v));
				});
				if (emotes.size() > 0) {
					tagsParsed.put(key, Collections.unmodifiableMap(emotes));
				}
			} else {
				tagsParsed.put(formatKey(key), formatValue(value));
			}
		});

		return new TagsV3(tagsParsed);
	}

	private static String formatValue(String value) {
		return value.replace("\\s", " ");
	}

	private static String formatKey(String key) {
		return key.replace("-", "_");
	}
}
