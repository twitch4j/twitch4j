package me.philippheuer.twitch4j.events.event.irc;

import lombok.*;
import me.philippheuer.twitch4j.events.Event;
import me.philippheuer.twitch4j.message.commands.CommandPermission;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This event gets called when we receive a raw irc message.
 *
 * @author Damian Staszewski [https://github.com/stachu540]
 * @version %I%, %G%
 * @since 1.0
 */
@Data
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class IRCMessageEvent extends Event {

	/**
	 * Tags
	 */
	private Map<String, String> tags = new HashMap<>();

	/**
	 * Badges
	 */
	private Map<String, String> badges = new HashMap<>();

	/**
	 * Client
	 */
	private Optional<String> clientName = Optional.empty();

	/**
	 * Message Type
	 */
	private String commandType = "UNKNOWN";

	/**
	 * Channel
	 */
	private Optional<String> channelName = Optional.empty();

	/**
	 * Message
	 */
	private Optional<String> message = Optional.empty();

	/**
	 * IRC Command Payload
	 */
	private Optional<String> payload = Optional.empty();

	/**
	 * Client Permissions
	 */
	private final Set<CommandPermission> clientPermissions = new HashSet<CommandPermission>();

	/**
	 * RAW Message
	 */
	private final String rawMessage;

	/**
	 * Event Constructor
	 *
	 * @param rawMessage      The raw message.
	 */
	public IRCMessageEvent(String rawMessage) {
		this.rawMessage = rawMessage;

		this.parseRawMessage();
		this.parsePermissions();
	}

	/**
	 * Checks if the Event was parsed correctly.
	 * @return Is the Event valid?
	 */
	public Boolean isValid() {
		return !getCommandType().equals("UNKNOWN");
	}

	/**
	 * Parse RAW Message
	 */
	@SuppressWarnings("unchecked")
	private void parseRawMessage() {
		// Parse Message
		Pattern pattern = Pattern.compile("^(?:@(?<tags>.+?) )?(?<clientName>.+?)(?: (?<command>[A-Z0-9]+) )(?:#(?<channel>.*?) ?)?(?<payload>[:\\-\\+](?<message>.+))?$");
		Matcher matcher = pattern.matcher(rawMessage);

		if(matcher.matches()) {
			// Parse Tags
			setTags(parseTags(matcher.group("tags")));
			if(getTags().containsKey("badges")) {
				setBadges(parseBadges(getTags().get("badges")));
			}

			setClientName(parseClientName(matcher.group("clientName")));
			setCommandType(matcher.group("command"));
			setChannelName(Optional.ofNullable(matcher.group("channel")));
			setMessage(Optional.ofNullable(matcher.group("message")));
			setPayload(Optional.ofNullable(matcher.group("payload")));
			return;
		}

		// Parse Message - Whisper
		Pattern patternPM = Pattern.compile("^(?:@(?<tags>.+?) )?:(?<clientName>.+?)!.+?(?: (?<command>[A-Z0-9]+) )(?:(?<channel>.*?) ?)??(?<payload>[:\\-\\+](?<message>.+))$");
		Matcher matcherPM = patternPM.matcher(rawMessage);

		if(matcherPM.matches()) {
			// Parse Tags
			setTags(parseTags(matcherPM.group("tags")));
			if(getTags().containsKey("badges")) {
				setBadges(parseBadges(getTags().get("badges")));
			}

			setClientName(parseClientName(matcherPM.group("clientName")));
			setCommandType(matcherPM.group("command"));
			setChannelName(Optional.ofNullable(matcherPM.group("channel")));
			setMessage(Optional.ofNullable(matcherPM.group("message")));
			setPayload(Optional.ofNullable(matcherPM.group("payload")));
		}
	}

	/**
	 * Parse Tags from raw list
	 *
	 * @param raw The raw list of tags.
	 * @return A key-value map of the tags.
	 */
	public Map parseTags(String raw) {
		Map<String, String> map = new HashMap<>();
		if(StringUtils.isBlank(raw)) return map;

		for (String tag: raw.split(";")) {
			String[] val = tag.split("=");
			final String key = val[0];
			String value = (val.length > 1) ? val[1] : null;
			map.put(key, value);
		}

		return Collections.unmodifiableMap(map); // formatting to Read-Only Map
	}

	/**
	 * Parse Badges from raw list
	 *
	 * @param raw The raw list of tags.
	 * @return A key-value map of the tags.
	 */
	public Map parseBadges(String raw) {
		Map<String, String> map = new HashMap<>();
		if(StringUtils.isBlank(raw)) return map;

		// Fix Whitespaces
		raw = raw.replace("\\s", " ");

		for (String tag: raw.split(",")) {
			String[] val = tag.split("/");
			final String key = val[0];
			String value = (val.length > 1) ? val[1] : null;
			map.put(key, value);
		}

		return Collections.unmodifiableMap(map); // formatting to Read-Only Map
	}

	/**
	 * Gets the ClientName from the IRC User Identifier (:user!user@user.tmi.twitch.tv)
	 *
	 * @param raw Raw ClientName
	 * @return Client name, or empty.
	 */
	public Optional<String> parseClientName(String raw) {
		if(raw.equals(":tmi.twitch.tv") || raw.equals(":jtv")) {
			return Optional.empty();
		}

		Pattern pattern = Pattern.compile("^:(.*?)!(.*?)@(.*?).tmi.twitch.tv$");
		Matcher matcher = pattern.matcher(raw);
		if(matcher.matches()) {
			return Optional.ofNullable(matcher.group(1));
		}

		return Optional.ofNullable(raw);
	}

	/**
	 * Gets a users permissions based on the raw message
	 */
	public void parsePermissions() {
		// Check for Permissions
		if (getTags().containsKey("badges")) {
			Boolean isChannelOwner = getTags().containsKey("user-id") && getTags().containsKey("room-id") && getTags().get("user-id").equals(getTags().get("room-id"));

			// - Broadcaster
			if (getBadges().containsKey("broadcaster") || isChannelOwner) {
				getClientPermissions().add(CommandPermission.BROADCASTER);
				getClientPermissions().add(CommandPermission.MODERATOR);
			}
			// Twitch Prime
			if (getBadges().containsKey("premium")) {
				getClientPermissions().add(CommandPermission.PRIME_TURBO);
			}
			// Partner
			if (getBadges().containsKey("partner")) {
				getClientPermissions().add(CommandPermission.PARTNER);
			}
		}
		// Moderator
		if (getTags().containsKey("mod") && getTags().get("mod").equals("1")) {
			getClientPermissions().add(CommandPermission.MODERATOR);
		}
		// Twitch Turbo
		if (getTags().containsKey("turbo") && getTags().get("turbo").equals("1")) {
			getClientPermissions().add(CommandPermission.PRIME_TURBO);
		}
		// Subscriber
		if (getTags().containsKey("subscriber") && getTags().get("subscriber").equals("1")) {
			getClientPermissions().add(CommandPermission.SUBSCRIBER);
		}
		// Everyone
		getClientPermissions().add(CommandPermission.EVERYONE);
	}

	/**
	 * Gets the Channel Id (from Tags)
	 */
	public Long getChannelId() {
		if(getTags().containsKey("room-id")) {
			return Long.parseLong(getTags().get("room-id"));
		}

		return null;
	}

	/**
	 * Gets the User Id (from Tags)
	 */
	public Long getUserId() {
		if(getTags().containsKey("user-id")) {
			return Long.parseLong(getTags().get("user-id"));
		}

		return null;
	}

	/**
	 * Gets a optional tag from the irc message
	 */
	public Optional<String> getTagValue(String tagName) {
		if(getTags().containsKey(tagName)) {
			String value = getTags().get(tagName);
			if(StringUtils.isBlank(value)) return Optional.empty();

			value = value.replaceAll("\\\\s", " ");
			return Optional.ofNullable(value);
		}

		return Optional.empty();
	}

}
