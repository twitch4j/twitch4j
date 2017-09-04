package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;
import me.philippheuer.twitch4j.message.commands.CommandPermission;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IRC Parser for Twitch Message Interface
 * @author Damian Staszewski
 */
@Getter
@SuppressWarnings("unchecked")
public class IRCParser {

	/**
	 * Raw Message using {@link Matcher} pattern
	 */
	private final Matcher matcher;
	private final IRCTags tags;

	/**
	 * IRC Parser for Twitch IRC-WS
	 * @param message Raw Message received from IRC-WS
	 */
	public IRCParser(String message) {
		final Pattern MESSAGE_REGEX = Pattern.compile("^(?:@(?<tags>.*?) )?(?::(?:(?:.*)!(?<user>.*)@(?:.*))?tmi.twitch.tv )?(?<command>[A-Z]+|[0-9]{3}) (?<message>.*)$");
		final Matcher matcher = MESSAGE_REGEX.matcher(message);

		// triggering matcher
		matcher.find();

		this.matcher = matcher;

		if (matcher.group("tags") != null) {
			tags = new IRCTags(matcher.group("tags"));
		} else {
			tags = null;
		}
	}

	/**
	 * Getting stringify message
	 * @return IRC format Message
	 */
	@Override
	public String toString() {
		switch (getTwitchCommandType().toUpperCase()) { // Handle each type different
			case "PRIVMSG": // User Message from specified joined Channel
				return String.format("[%s] [#%s] %s: %s", getCommand(), getChannelName(), getUserName(), getMessage());
			case "ACTION": // someone on the chat use '/me'
				return String.format("[%s] [#%s] %s %s", getCommand(), getChannelName(), getUserName(), getMessage());
			case "WHISPER": // Whisper from User
				return String.format("[%s] %s: %s", getCommand(), getUserName(), getMessage());
			case "JOIN": // User Join Channel
			case "PART": // User Leave Channel
				return String.format("[%s] [#%s] @%s", getCommand(), getChannelName(), getUserName());
			case "USERSTATE": // User status
			case "ROOMSTATE": // Channel status
				return String.format("[%s] [#%s] @T=%s", getCommand(), getChannelName(), tags.toString());
			case "GLOBALUSERSTATE":  // Global user state shows connection established
				return String.format("[%s] @T=%s", getCommand(), tags.toString());
			case "USERNOTICE": // Subscribe notification
				boolean isResub = tags.getTag("msg-id").toString().equalsIgnoreCase("resub");
				int months = (isResub) ? Integer.parseInt((String) tags.getTag("msg-param-months")) : 1;
				String plan = tags.getTag("msg-param-sub-plan").toString();
				String subType = (isResub) ? String.format("[%s|%s]", tags.getTag("msg-id").toString().toUpperCase(), String.valueOf(months)) : "[" + tags.getTag("msg-id") + "]";
				String formatMsg = String.format("[%s] %s %s", plan, subType, getUserName() + ((isResub) ? ": " + getMessage() : ""));
				return String.format("[%s] [#%s] %s", getCommand(), getChannelName(), formatMsg);
			case "MODE": // Permissions Mode (+o)
				String channelName = matcher.group(6).substring(0, matcher.group(6).indexOf(" "));
				String message = matcher.group(6).substring(matcher.group(6).indexOf(" ") + 1);
				return String.format("[%s] [%s] %s", getCommand(), channelName, message);
			case "BAN": // User banned on channel
			case "TIMEOUT": // User timeouted on channel
				String reason = (String) tags.getTag("ban-reason");
				if (tags.hasTag("ban-duration"))
					return String.format("[TIMEOUT] [#%s] %s @%s", getChannelName(), tags.getTag("ban-duration").toString(), getUserName() + ((reason != null) ? " :" + reason : ""));
				return String.format("[BAN] [#%s] %s @%s", getChannelName(), tags.getTag("ban-duration").toString(), getUserName() + ((reason != null) ? " :" + reason : ""));
			case "CLEARCHAT": // Clearing chat on channel
				return String.format("[%s] [#%s]", getCommand(), getChannelName());
			case "NOTICE": // Server Notice
				return String.format("[%s] [#%s] [%s] :%s", getCommand(), getChannelName(), tags.getTag("msg-id").toString(), getMessage());
			case "CAP": // Tags capturing
				return String.format("[%s %s] :%s", getCommand(), matcher.group(6), getMessage());
			default: // Default Case
				return String.format("[%s]%s", getCommand(), (getMessage() != null) ? " :" + getMessage() : "");
		}
	}

	/**
	 * Gets the raw message
	 *
	 * @return the raw message.
	 */
	public String getRawMessage() {
		return matcher.group(0);
	}

	/**
	 * Getting the id of the user triggering this event
	 *
	 * @return the id of the user
	 */
	public Long getUserId() {
	    if (tags.hasTag("user-id")) {
			return Long.parseLong((String) tags.getTag("user-id"));
		}

		return null;
	}

	/**
	 * Getting the name of the user triggering this event
	 *
	 * @return display name of the user
	 */
	public String getUserName() {
		/*
		if (getCommand().equalsIgnoreCase("CLEARCHAT")) {
			return getMessage();
		} else {
			return matcher.group("user");
		}
		*/
		return matcher.group("user");
	}

	/**
	 * Get the channel name
	 * @return the channel the message originates from
	 */
	public String getChannelName() {
		if (matcher.group("message").startsWith("#")) {
			return matcher.group("message").split(" ")[0].substring(1);
		}

		return null;
	}

	public String getTwitchCommandType() {
		if("PRIVMSG".equals(getCommand().toUpperCase())) {
			if (matcher.group("message").contains("ACTION"))
				return "ACTION"; // using `/me` by chat user
		}
		else if("CLEARCHAT".equals(getCommand().toUpperCase())) {
			if (tags.hasTag("ban-reason")) {
				if (tags.hasTag("ban-duration")) {
					return "TIMEOUT";
				} else {
					return "BAN";
				}
			}
		}

		return getCommand();
	}

	/**
	 * Getting command
	 * @return IRC Received Command
	 */
	public String getCommand() {
		return matcher.group("command");
	}

	/**
	 * Getting message
	 * @return IRC Message
	 */
	public String getMessage() {
		String rawMsg = matcher.group("message");

		if(rawMsg.length() > getChannelName().length() + 3) {
			String msg = rawMsg.substring(getChannelName().length() + 3);

			if (msg != null && msg.contains("ACTION")) {
				return msg.substring(8, msg.length() - 1);
			} else if (getCommand().equalsIgnoreCase("HOSTTARGET") || getCommand().equalsIgnoreCase("MODE"))  {
				return (msg != null) ? msg : matcher.group(6).substring(2 + getChannelName().length());
			}

			return msg;
		}

		return null;
	}

	/**
	 * Get User Permissions using the IRC Tags
	 * @return list {@link CommandPermission} user
	 */
	public Set<CommandPermission> getPermissions() {
		Set<CommandPermission> userPermissions = new HashSet<>();

		// Check for Permissions
		if (tags.hasTag("badges")) {
			HashMap<String, String> badges = (HashMap) tags.getTag("badges");

			if(badges != null) {
				// - Broadcaster
				if (badges.containsKey("broadcaster")) {
					userPermissions.add(CommandPermission.BROADCASTER);
					userPermissions.add(CommandPermission.MODERATOR);
				}
				// Twitch Prime
				if (badges.containsKey("premium")) {
					userPermissions.add(CommandPermission.PRIME_TURBO);
				}
				// Moderator
				if (badges.containsKey("moderator")) {
					userPermissions.add(CommandPermission.MODERATOR);
				}
				// Partner
				if (badges.containsKey("partner")) {
					userPermissions.add(CommandPermission.PARTNER);
				}
			}
		}
		// Twitch Turbo
		if (tags.hasTag("turbo") && tags.getTag("turbo").equals("1")) {
			userPermissions.add(CommandPermission.PRIME_TURBO);
		}
		// Subscriber
		if (tags.hasTag("subscriber") && tags.getTag("subscriber").equals("1")) {
			userPermissions.add(CommandPermission.SUBSCRIBER);
		}
		// Everyone
		userPermissions.add(CommandPermission.EVERYONE);

		return userPermissions;
	}
}
