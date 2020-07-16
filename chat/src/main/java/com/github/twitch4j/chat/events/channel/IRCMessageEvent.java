package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.chat.flag.FlagParser;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.TwitchUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This event gets called when we receive a raw irc message.
 */
@Data
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class IRCMessageEvent extends TwitchEvent {

	/**
	 * Tags
	 */
	private Map<String, String> tags = new HashMap<>();

    /**
     * Raw Tags
     */
    private Map<String, Object> rawTags = new HashMap<>();

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
	private final Set<CommandPermission> clientPermissions = EnumSet.noneOf(CommandPermission.class);

    /**
     * AutoMod Message Flag Indicators, relevant for PRIVMSG and USERNOTICE
     */
    @Unofficial
    @Getter(lazy = true)
    private final List<AutoModFlag> flags = FlagParser.parseFlags(this);

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

		// permissions
		getClientPermissions().addAll(TwitchUtils.getPermissionsFromTags(getRawTags()));
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
            setRawTags(parseTags(matcher.group("tags")));
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
			setRawTags(parseTags(matcherPM.group("tags")));
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
	 * Gets the Channel Id (from Tags)
     *
     * @return Long channelId
	 */
	public String getChannelId() {
		if(getTags().containsKey("room-id")) {
			return getTags().get("room-id");
		}

		return null;
	}

	/**
	 * Gets the User Id (from Tags)
     *
     * @return Long userId
	 */
	public String getUserId() {
		if(getTags().containsKey("user-id")) {
			return getTags().get("user-id");
		}

		return null;
	}

	/**
	 * Gets the User Name (from Tags)
     *
     * @return String userName
	 */
	public String getUserName() {
		if(getTags().containsKey("login")) {
			return getTags().get("login");
		}

		return getClientName().orElse(null);
	}

    /**
     * Gets the Target User Id (from Tags)
     *
     * @return Long targetUserId
     */
    public String getTargetUserId() {
        if(getTags().containsKey("target-user-id")) {
            return getTags().get("target-user-id");
        }

        return null;
    }

	/**
	 * Gets a optional tag from the irc message
     *
     * @param tagName The tag of the irc message
     * @return String tagValue
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

	/**
	 * Get User
     *
     * @return ChatUser
	 */
	public EventUser getUser() {
	    if (getUserId() != null || getUserName() != null) {
            return new EventUser(getUserId(), getUserName());
        }

		return null;
	}

    /**
     * Get Target User
     *
     * @return ChatUser
     */
    public EventUser getTargetUser() {
        return new EventUser(getTargetUserId(), getCommandType().equalsIgnoreCase("CLEARCHAT") ? getMessage().get() : null);
    }


	/**
	 * Get ChatChannel
     *
     * @return ChatChannel
	 */
	public EventChannel getChannel() {
		return new EventChannel(getChannelId(), getChannelName().get());
	}

}
