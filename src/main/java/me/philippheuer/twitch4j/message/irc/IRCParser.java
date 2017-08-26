package me.philippheuer.twitch4j.message.irc;

import lombok.Getter;
import me.philippheuer.twitch4j.message.commands.CommandPermission;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		final Pattern MESSAGE_REGEX = Pattern.compile("^(?:@(.*?) )?(?::(.+?)(?:!(.+?))?(?:@(.+?))? )?((?:[A-z]+)|(?:[0-9]{3}))(?: (?!:)(.+?))?(?: :(.*))?$");
		final Matcher matcher = MESSAGE_REGEX.matcher(message);
		matcher.find(); // triggering matcher (he returns boolean)
		this.matcher = matcher;
		if (matcher.group(0) != null) tags = new IRCTags(matcher.group(0));
		else tags = null;
		// testing matcher's - uncomment it for test
//		for (int o = 1; matcher.groupCount() >= o; ++o) {
//			System.out.println(matcher.group(o));
//		}
	}

	/**
	 * Getting stringify message
	 * @return IRC format Message
	 */
	@Override
	public String toString() {
		switch (getFormedCommand().toUpperCase()) { // Handle each type different
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
				return String.format("[%s] [#%s] @T=%s", getCommand(), getChannelName(), tags.getTags().toString());
			case "GLOBALUSERSTATE":  // Global user state shows connection established
				return String.format("[%s] @T=%s", getCommand(), tags.getTags().toString());
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
				else return String.format("[BAN] [#%s] %s @%s", getChannelName(), tags.getTag("ban-duration").toString(), getUserName() + ((reason != null) ? " :" + reason : ""));
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
		if (getCommand().equalsIgnoreCase("CLEARCHAT") && matcher.group(7) != null)
			return matcher.group(7);
		else return matcher.group(3);
	}

	/**
	 * Getting the name of the user triggering this event
	 *
	 * @return display name of the user
	 */
	public String getDisplayName() {
		if (tags.hasTag("display-name")) {
			return (String) tags.getTag("display-name");
		}

		return null;
	}

	/**
	 * Get the channel name
	 * @return the channel the message originates from
	 */
	public String getChannelName() {
		if (matcher.group(6).startsWith("#")) {
			return matcher.group(6).substring(1);
		}
		return null;
	}

	public String getFormedCommand() {
		switch (getCommand().toUpperCase()) {
			case "PRIVMSG":
				if (matcher.group(7).contains("ACTION")) return "ACTION"; // using `/me` by chat user
				return getCommand();
			case "CLEARCHAT": // clear chat have 3 ways
				if (matcher.group(7).contains("ACTION")) return "ACTION";
				if (tags.hasTag("ban-reason")) {
					if (tags.hasTag("ban-duration"))
						return "TIMEOUT"; // timeouting
					else return "BAN"; // banning
				}
				return getCommand(); // chat clearing
			default:
				return getCommand();
		}
	}

	/**
	 * Getting command
	 * @return IRC Received Command
	 */
	public String getCommand() {
		return matcher.group(5);
	}

	/**
	 * Getting message
	 * @return IRC Message
	 */
	public String getMessage() {
		if (!getCommand().equalsIgnoreCase("CLEARCHAT")) {
			String msg = matcher.group(7);
			if (msg.contains("ACTION")) {
				return matcher.group(7).substring(8, matcher.group(7).length() - 1);
			} else return msg;
		}

		return null;
	}

	/**
	 * Get User Permissions using the IRC Tags
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
