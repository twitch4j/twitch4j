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
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * This event gets called when we receive a raw irc message.
 */
@Data
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
public class IRCMessageEvent extends TwitchEvent {

    private static final Pattern MESSAGE_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?(?<clientName>\\S+?)\\s(?<command>[A-Z0-9]+)\\s?(?:(?<login>\\S+)\\s=\\s)?(?:#(?<channel>\\S*?)\\s?)?(?<payload>[:\\-+](?<message>.+))?$");
    private static final Pattern WHISPER_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?:(?<clientName>\\S+?)!.+?\\s(?<command>[A-Z0-9]+)\\s(?:(?<channel>\\S*?)\\s?)??(?<payload>[:\\-+](?<message>.+))$");
    private static final Pattern CLIENT_PATTERN = Pattern.compile("^:(.*?)!(.*?)@(.*?).tmi.twitch.tv$");

    @Unofficial
    public static final String NONCE_TAG_NAME = "client-nonce";

    /**
     * Tags
     * <p>
     * Most applications should utilize {@link #getTagValue(String)} rather than accessing this map directly.
     */
    private Map<String, String> tags = new HashMap<>();

	/**
	 * Badges
	 */
	private Map<String, String> badges = new HashMap<>();

    /**
     * Metadata related to the chat badges in the badges tag
     */
    private Map<String, String> badgeInfo = null;

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
        if (channelId == null) {
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
    }

    /**
     * @return metadata related to the chat badges in the badges tag
     */
    public Map<String, String> getBadgeInfo() {
        Map<String, String> info = badgeInfo;
        if (info == null) {
            this.badgeInfo = info = new HashMap<>();
            getTagValue("badge-info").map(TwitchUtils::parseBadges).ifPresent(map -> badgeInfo.putAll(map));
        }
        return info;
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
    @ApiStatus.Internal
	public Map<String, String> parseTags(String raw) {
		Map<String, String> map = new HashMap<>();
		if(StringUtils.isBlank(raw)) return map;

		for (String tag: raw.split(";")) {
			String[] val = tag.split("=", 2);
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
		return tags.get("user-id");
	}

    /**
     * Gets the User Name (from Tags)
     *
     * @return String userName
     * @apiNote This getter returns the login name when available
     */
	public String getUserName() {
        String login = tags.get("login");
		if (login != null) {
			return login;
		}

		return getClientName()
            .filter(StringUtils::isNotBlank)
            .orElseGet(() -> getUserDisplayName().orElse(null));
	}

    /**
     * @return the display name of the user who sent this message, if applicable
     */
    public Optional<String> getUserDisplayName() {
        return getTagValue("display-name");
    }

    /**
     * @return hexadecimal RGB color code of the user's chat color, or empty if it is never set.
     */
    public Optional<String> getUserChatColor() {
        return getTagValue("color");
    }

    /**
     * Gets the Target User Id (from Tags)
     *
     * @return Long targetUserId
     */
    public String getTargetUserId() {
        return tags.get("target-user-id");
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
        Map<String, String> badgeInfo = getBadgeInfo();
        final String monthsStr = badgeInfo.getOrDefault("subscriber", badgeInfo.get("founder"));

        if (monthsStr != null) {
            try {
                return OptionalInt.of(Integer.parseInt(monthsStr));
            } catch (Exception ignored) {
            }
        }

        return OptionalInt.empty();
    }

    /**
     * @return the tier at which the user is subscribed, or empty if they are not subscribed or have the founders badge equipped
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
        return Optional.ofNullable(getBadgeInfo().get("predictions")).map(EscapeUtils::unescapeTagValue);
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

    /**
     * @return IRCv3 tags
     * @deprecated in favor of {@link #getTags()}
     */
    @Deprecated
    public Map<String, Object> getRawTags() {
        return Collections.unmodifiableMap(tags);
    }

}
