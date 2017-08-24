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

	/**
	 * IRC Parser for Twitch IRC-WS
	 * @param message Raw Message received from IRC-WS
	 */
	public IRCParser(String message) {
		final Pattern MESSAGE_REGEX = Pattern.compile("^(?:@(.*?) )?(?::(.+?)(?:!(.+?))?(?:@(.+?))? )?((?:[A-z]+)|(?:[0-9]{3}))(?: (?!:)(.+?))?(?: :(.*))?$");
		final Matcher matcher = MESSAGE_REGEX.matcher(message);
		matcher.find(); // triggering matcher (he returns boolean)
		this.matcher = matcher;
	}

	/**
	 * Getting stringify message
	 * @return IRC format Message
	 */
	@Override
	public String toString() {
		switch (getCommand().toUpperCase()) { // Handle each type different
			case "PRIVMSG": // User Message from specified joined Channel
				return String.format("[%s] [#%s] %s: %s", getCommand(), getChannelName(), getUserName(), getMessage());
			case "WHISPER": // Whisper from User
				return String.format("[%s] %s: %s", getCommand(), getUserName(), getMessage());
			case "JOIN": // User Join Channel
			case "PART": // User Leave Channel
				return String.format("[%s] [#%s] @%s", getCommand(), getChannelName(), getUserName());
			case "USERSTATE": // User status
			case "ROOMSTATE": // Channel status
				return String.format("[%s] [#%s] @T=%s", getCommand(), getChannelName(), getTags().toString());
			case "GLOBALUSERSTATE":  // Global user state shows connection established
				return String.format("[%s] @T=%s", getCommand(), getTags().toString());
			case "USERNOTICE": // Subscribe notification
				boolean isResub = getTag("msg-id").toString().equalsIgnoreCase("resub");
				int months = (isResub) ? Integer.parseInt(getTag("msg-param-months")) : 1;
				String plan = getTag("msg-param-sub-plan").toString();

				String subType = (isResub) ? String.format("[%s|%s]", getTag("msg-id").toString().toUpperCase(), String.valueOf(months)) : "[" + getTag("msg-id") + "]";
				String formatMsg = String.format("[%s] %s %s", plan, subType , getUserName() + ((isResub) ? ": " + getMessage() : ""));
				return String.format("[%s] [#%s] %s", getCommand(), getChannelName(), subType );
			case "MODE": // Permissions Mode (+o)
				String channelName = matcher.group(6).substring(0, matcher.group(6).indexOf(" "));
				String message = matcher.group(6).substring(matcher.group(6).indexOf(" ") + 1);
				return String.format("[%s] [%s] %s", getCommand(), channelName, message);
			case "CLEARCHAT": // Clearing chat for specify user (only for banning or timeouting)
				return String.format("[%s] [#%s] @%s", getCommand(), getChannelName(), getUserName());
			case "NOTICE": // Server Notice
				return String.format("[%s] [#%s] [%s] :%s", getCommand(), getChannelName(), getTag("msg-id").toString(), getMessage());
			case "CAP": // Tags capturing
				return String.format("[%s %s] :%s", getCommand(), matcher.group(6), getMessage());
			default: // Default Case
				return String.format("[%s]%s", getCommand(), (getMessage() != null) ? " :" + getMessage() : "");
		}
	}

	/**
	 * Gets the TagList of the IRC Message
	 * @param <T> some classes extends Object
	 * @return {@link Map} tag list
	 */
	public <T extends Object> Map<String, T> getTags() {
		Map<String, T> tagList = new HashMap<>();
		String rawTags = matcher.group(1);

		for (String tagData: rawTags.split(";")) {
			String key = tagData.split("=")[0];

			if(tagData.split("=").length == 1) {
				// No Value
				T value = parseTagValue(tagData.split("=")[0], null); // should be null
				tagList.put(key, value);
			} else {
				// Typed Value
				T value = parseTagValue(tagData.split("=")[0], tagData.split("=")[1]);
				tagList.put(key, value);
			}
		}

		return tagList;
	}

	/**
	 * Parsing tag value
	 * @param key key
	 * @param value value
	 * @param <T> some classes extends Object
	 * @return value converted to Object class
	 */
	private <T extends Object> T parseTagValue(String key, String value) {
		if (value == null) return null;
		try {
			if (key.equalsIgnoreCase("badges")) {
				Map<String, Integer> badges = new HashMap<String, Integer>();

				if((value.contains(",") || value.contains("/"))) {
					for (String badge : value.split(",")) {
						badges.put(badge.split("/")[0], Integer.parseInt(badge.split("/")[1]));
					}
				}

				return (T) badges;
			} else if ((value.contains("/") || value.contains(":")) && key.equalsIgnoreCase("emotes")) {
				Map<Integer, Map.Entry<Integer, Integer>> emotes = new HashMap<Integer, Map.Entry<Integer, Integer>>();
				for (String emote : value.split("/")) {
					emotes.put(Integer.parseInt(emote.split(":")[0]), new AbstractMap.SimpleEntry<>(Integer.parseInt(emote.split(":")[1].split("-")[0]), Integer.parseInt(emote.split(":")[1].split("-")[1])));
				}
				return (T) emotes;
			} else if (key.equalsIgnoreCase("subscriber") || key.equalsIgnoreCase("mod") || key.equalsIgnoreCase("turbo")) {
				return (T) Boolean.valueOf(value.equals("1"));
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
		}
        return (T) value;
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
	 * Getting tag exists
	 *
	 * @param name tag name
	 * @param <T> some classes extends Object
	 * @return tag existence
	 */
	public <T extends Object> Boolean hasTag(String name) {
		Map<String, T> tags = getTags();
		return !tags.isEmpty() && tags.containsKey(name);
	}

	/**
	 * Get one tag
	 *
	 * @param name tag name
	 * @param <T> some classes extends Object
	 * @return getting tag data returned classes extends Object
	 */
	public <T extends Object> T getTag(String name) {
		return (T) getTags().get(name);
	}

	/**
	 * Getting the id of the user triggering this event
	 *
	 * @return the id of the user
	 */
	public Long getUserId() {
	    if (hasTag("user-id")) {
			return Long.parseLong(getTag("user-id"));
		}

		return null;
	}

	/**
	 * Getting the name of the user triggering this event
	 *
	 * @return display name of the user
	 */
	public String getUserName() {
		if (hasTag("display-name")) {
			return (String) getTag("display-name");
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
			return matcher.group(7);
		}

		return null;
	}

	/**
	 * Get User Permissions using the IRC Tags
	 */
	public Set<CommandPermission> getPermissions() {
		Set<CommandPermission> userPermissions = new HashSet<>();

		// Check for Permissions
		if (getTags().containsKey("badges")) {
			HashMap<String, String> badges = (HashMap)getTags().get("badges");

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
		// Twitch Turbo
		if (getTags().containsKey("turbo") && getTags().get("turbo").equals("1")) {
			userPermissions.add(CommandPermission.PRIME_TURBO);
		}
		// Subscriber
		if (getTags().containsKey("subscriber") && getTags().get("subscriber").equals("1")) {
			userPermissions.add(CommandPermission.SUBSCRIBER);
		}
		// Everyone
		userPermissions.add(CommandPermission.EVERYONE);

		return userPermissions;
	}
}
