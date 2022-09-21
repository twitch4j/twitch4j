package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.chat.flag.FlagParser;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.EscapeUtils;
import com.github.twitch4j.common.util.TwitchUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

/**
 * This event gets called when we receive a raw irc message.
 */
@Data
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class IRCMessageEvent extends TwitchEvent {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?(?<clientName>\\S+?)\\s(?<command>[A-Z0-9]+)\\s?(?:#(?<channel>\\S*?)\\s?)?(?<payload>[:\\-+](?<message>.+))?$");
    private static final Pattern WHISPER_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?:(?<clientName>\\S+?)!.+?\\s(?<command>[A-Z0-9]+)\\s(?:(?<channel>\\S*?)\\s?)??(?<payload>[:\\-+](?<message>.+))$");
    private static final Pattern CLIENT_PATTERN = Pattern.compile("^:(.*?)!(.*?)@(.*?).tmi.twitch.tv$");

    @Unofficial
    public static final String NONCE_TAG_NAME = "client-nonce";

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
     * Metadata related to the chat badges in the badges tag
     */
    private Map<String, String> badgeInfo = new HashMap<>();

	/**
	 * Client
	 */
	private Optional<String> clientName = Optional.empty();

	/**
	 * Message Type
	 */
	private String commandType = "UNKNOWN";

    /**
     * Channel Id
     */
    private String channelId;

	/**
	 * Channel Name
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
     * @param rawMessage  The raw message.
     * @param channelIdToChannelName Mapping used to lookup a missing channel name in the event
     * @param channelNameToChannelId Mapping used to lookup a missing channel id in the event
     * @param botOwnerIds The bot owner ids.
     */
	public IRCMessageEvent(String rawMessage, Map<String, String> channelIdToChannelName, Map<String, String> channelNameToChannelId, Collection<String> botOwnerIds) {
		this.rawMessage = rawMessage;

		this.parseRawMessage();

        // set channel id
        if (tags.containsKey("room-id")) {
            channelId = tags.get("room-id");
        }

        // provide channel id or name from cache if the event was missing one
        if (!channelName.isPresent() && channelId != null) {
            channelName = Optional.ofNullable(channelIdToChannelName.get(channelId));
        } else if (channelName.isPresent() && channelId == null) {
            channelId = channelNameToChannelId.get(channelName.get());
        }

        // permissions and badges
		getClientPermissions().addAll(TwitchUtils.getPermissionsFromTags(getRawTags(), badges, botOwnerIds != null ? getUserId() : null, botOwnerIds));
		getTagValue("badge-info").map(TwitchUtils::parseBadges).ifPresent(map -> badgeInfo.putAll(map));
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
		Matcher matcher = MESSAGE_PATTERN.matcher(rawMessage);
		if (matcher.matches()) {
			// Parse Tags
			tags = parseTags(matcher.group("tags"));
            rawTags = parseTags(matcher.group("tags"));
			clientName = parseClientName(matcher.group("clientName"));
			commandType = matcher.group("command");
			channelName = Optional.ofNullable(matcher.group("channel"));
			message = Optional.ofNullable(matcher.group("message"));
			payload = Optional.ofNullable(matcher.group("payload"));
			return;
		}

		// Parse Message - Whisper
		Matcher matcherPM = WHISPER_PATTERN.matcher(rawMessage);
		if (matcherPM.matches()) {
			// Parse Tags
			tags = parseTags(matcherPM.group("tags"));
			rawTags = parseTags(matcherPM.group("tags"));
			clientName = parseClientName(matcherPM.group("clientName"));
			commandType = matcherPM.group("command");
			channelName = Optional.ofNullable(matcherPM.group("channel"));
			message = Optional.ofNullable(matcherPM.group("message"));
			payload = Optional.ofNullable(matcherPM.group("payload"));
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

		Matcher matcher = CLIENT_PATTERN.matcher(raw);
		if(matcher.matches()) {
			return Optional.ofNullable(matcher.group(1));
		}

		return Optional.ofNullable(raw);
	}

	/**
	 * Gets the User Id (from Tags)
     *
     * @return Long userId
	 */
	public String getUserId() {
		if (tags.containsKey("user-id")) {
			return tags.get("user-id");
		}

		return null;
	}

	/**
	 * Gets the User Name (from Tags)
     *
     * @return String userName
	 */
	public String getUserName() {
		if (tags.containsKey("login")) {
			return tags.get("login");
		}

		return getClientName()
            .filter(StringUtils::isNotBlank)
            .orElseGet(() -> getTagValue("display-name").orElse(null));
	}

    /**
     * Gets the Target User Id (from Tags)
     *
     * @return Long targetUserId
     */
    public String getTargetUserId() {
        if (tags.containsKey("target-user-id")) {
            return tags.get("target-user-id");
        }

        return null;
    }

    /**
     * The message UUID that is used for deletion by a moderator or a chat reply (from Tags)
     *
     * @return the unique ID for the message
     */
    public Optional<String> getMessageId() {
        return getTagValue("id");
    }

    /**
     * @return the client nonce for the message.
     */
    @Unofficial
    public Optional<String> getNonce() {
        return getTagValue(NONCE_TAG_NAME);
    }

    /**
     * @return the exact number of months the user has been a subscriber, or empty if they are not subscribed
     */
    public OptionalInt getSubscriberMonths() {
        final String monthsStr = badgeInfo.get("subscriber");

        if (monthsStr != null) {
            try {
                return OptionalInt.of(Integer.parseInt(monthsStr));
            } catch (Exception ignored) {
            }
        }

        return OptionalInt.empty();
    }

    /**
     * @return the tier at which the user is subscribed, or empty if they are not subscribed
     */
    public OptionalInt getSubscriptionTier() {
        final String subscriber = badges.get("subscriber");

        if (subscriber != null) {
            try {
                return OptionalInt.of(Math.max(Integer.parseInt(subscriber) / 1000, 1));
            } catch (Exception ignored) {
            }
        }

        return OptionalInt.empty();
    }

    /**
     * @return the tier of the bits badge of the user, or empty if there is no bits badge present (which can also occur for bits leaders)
     */
    public OptionalInt getCheererTier() {
        final String bits = badges.get("bits");

        if (bits != null) {
            try {
                return OptionalInt.of(Integer.parseInt(bits));
            } catch (NumberFormatException ignored) {
            }
        }

        return OptionalInt.empty();
    }

    /**
     * @return the index of the choice this user is predicting, in an optional wrapper
     */
    public OptionalInt getPredictedChoiceIndex() {
        String predictions = badges.get("predictions");

        if (predictions != null) {
            int delim = predictions.indexOf('-');
            try {
                return OptionalInt.of(Integer.parseInt(predictions.substring(delim + 1)));
            } catch (NumberFormatException ignored) {
            }
        }

        return OptionalInt.empty();
    }

    /**
     * @return the title of the choice this user is predicting, in an optional wrapper
     */
    public Optional<String> getPredictedChoiceTitle() {
        return Optional.ofNullable(badgeInfo.get("predictions")).map(EscapeUtils::unescapeTagValue);
    }

	/**
	 * Gets a optional tag from the irc message
     *
     * @param tagName The tag of the irc message
     * @return String tagValue
	 */
	public Optional<String> getTagValue(String tagName) {
	    return Optional.ofNullable(tags.get(tagName))
            .filter(StringUtils::isNotBlank)
            .map(EscapeUtils::unescapeTagValue);
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
