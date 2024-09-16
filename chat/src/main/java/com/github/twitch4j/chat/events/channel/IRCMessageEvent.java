package com.github.twitch4j.chat.events.channel;

import com.github.twitch4j.chat.events.TwitchEvent;
import com.github.twitch4j.chat.flag.AutoModFlag;
import com.github.twitch4j.chat.flag.FlagParser;
import com.github.twitch4j.chat.util.MessageParser;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.common.util.CryptoUtils;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    private static final Pattern CLIENT_PATTERN = Pattern.compile("^:(.*?)!(.*?)@(.*?).tmi.twitch.tv$");

    @Unofficial
    public static final String NONCE_TAG_NAME = "client-nonce";

    /**
     * Tags
     * <p>
     * Most applications should utilize {@link #getTagValue(String)} rather than accessing this map directly.
     */
    private final Map<String, CharSequence> escapedTags;

    /**
     * Badges
     */
    private Map<String, String> badges = null;

    /**
     * Metadata related to the chat badges in the badges tag
     */
    private Map<String, String> badgeInfo = null;

    /**
     * Client
     */
    private final CharSequence clientName;

    /**
     * Message Type
     */
    private final String commandType;

    /**
     * Channel Id
     */
    private String channelId;

    /**
     * Channel Name
     */
    @Nullable
    private String channelName;

    /**
     * Message
     */
    @Nullable
    private final String message;

    /**
     * IRC Command Payload
     */
    private final CharSequence payload;

    /**
     * Client Permissions
     */
    private Set<CommandPermission> clientPermissions = null;

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

    @Nullable
    @Getter(AccessLevel.NONE)
    private final Collection<String> botOwnerIds;

    @ApiStatus.Internal
    public IRCMessageEvent(@NotNull String rawMessage, @NotNull Map<String, CharSequence> escapedTags, CharSequence clientName, @NotNull String commandType, @Nullable String channelName, @Nullable CharSequence payload, @Nullable String message,
                           @NotNull Map<String, String> channelIdToChannelName, @NotNull Map<String, String> channelNameToChannelId, @Nullable Collection<String> botOwnerIds) {
        super(getEventId(escapedTags), getEventTime(escapedTags));
        this.rawMessage = rawMessage;
        this.escapedTags = escapedTags;
        this.clientName = clientName;
        this.commandType = commandType;
        this.channelName = channelName;
        this.payload = payload;
        this.message = message;
        this.botOwnerIds = botOwnerIds;

        // set channel id and name from tags or cache
        if (channelName == null) {
            this.channelId = getRawTagString("room-id");
            this.channelName = channelId == null ? null : channelIdToChannelName.get(channelId);
        } else {
            this.channelId = channelNameToChannelId.get(channelName);
            if (channelId == null) {
                this.channelId = getRawTagString("room-id");
            }
        }
    }

    /**
     * Event Constructor
     *
     * @param rawMessage             The raw message.
     * @param channelIdToChannelName Mapping used to lookup a missing channel name in the event
     * @param channelNameToChannelId Mapping used to lookup a missing channel id in the event
     * @param botOwnerIds            The bot owner ids.
     * @deprecated Use {@link MessageParser#parse(String, Map, Map, Collection)} to create {@link IRCMessageEvent} instances
     */
    @Deprecated
    public IRCMessageEvent(String rawMessage, Map<String, String> channelIdToChannelName, Map<String, String> channelNameToChannelId, Collection<String> botOwnerIds) {
        this(Optional.ofNullable(MessageParser.parse(rawMessage, channelIdToChannelName, channelNameToChannelId, botOwnerIds)).orElse(failure(rawMessage)), channelIdToChannelName, channelNameToChannelId, botOwnerIds);
    }

    private IRCMessageEvent(@NotNull IRCMessageEvent copy, @NotNull Map<String, String> channelIdToChannelName, @NotNull Map<String, String> channelNameToChannelId, @Nullable Collection<String> botOwnerIds) {
        this(copy.rawMessage, copy.escapedTags, copy.clientName, copy.commandType, copy.channelName, copy.payload, copy.message, channelIdToChannelName, channelNameToChannelId, botOwnerIds);
    }

    private static IRCMessageEvent failure(String raw) {
        return new IRCMessageEvent(raw, Collections.emptyMap(), null, "UNKNOWN", null, null, null, Collections.emptyMap(), Collections.emptyMap(), Collections.emptySet());
    }

    /**
     * @return metadata related to the chat badges in the badges tag
     */
    public Map<String, String> getBadgeInfo() {
        Map<String, String> info = badgeInfo;
        if (info != null) return info;
        return this.badgeInfo = TwitchUtils.parseBadges(getRawTag("badge-info"));
    }

    /**
     * @return the visible chat badges parsed from tags
     */
    public Map<String, String> getBadges() {
        Map<String, String> badges = this.badges;
        if (badges == null) {
            this.badges = badges = new HashMap<>();
            this.clientPermissions = TwitchUtils.getPermissionsFromTags(getRawTags(), badges, botOwnerIds != null ? getUserId() : null, botOwnerIds);
        }
        return badges;
    }

    /**
     * @return the permissions of the client that sent this message, based on visible badges
     */
    public Set<CommandPermission> getClientPermissions() {
        if (clientPermissions == null) {
            getBadges(); // initializes clientPermissions
        }
        return clientPermissions;
    }

    /**
     * Checks if the Event was parsed correctly.
     *
     * @return Is the Event valid?
     * @deprecated {@link MessageParser#parse(String)} yields null for invalid messages
     */
    @Deprecated
    public Boolean isValid() {
        return !getCommandType().equals("UNKNOWN");
    }

    /**
     * Parse Tags from raw list
     *
     * @param raw The raw list of tags.
     * @return A key-value map of the tags.
     * @deprecated This method is no longer used by twitch4j
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public static Map<String, CharSequence> parseTags(String raw) {
        Map<String, CharSequence> map = new HashMap<>();
        if (StringUtils.isBlank(raw)) return map;

        for (String tag : raw.split(";")) {
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
     * @deprecated This method is no longer used by twitch4j
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
    public static Optional<String> parseClientName(String raw) {
        if (raw.equals(":tmi.twitch.tv") || raw.equals(":jtv")) {
            return Optional.empty();
        }

        Matcher matcher = CLIENT_PATTERN.matcher(raw);
        if (matcher.matches()) {
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
        return getRawTagString("user-id");
    }

    /**
     * Gets the User Name (from Tags)
     *
     * @return String userName
     * @apiNote This getter returns the login name when available
     */
    public String getUserName() {
        String login = getRawTagString("login");
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
        return getRawTagString("target-user-id");
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
        final String subscriber = getBadges().get("subscriber");

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
        final String bits = getBadges().get("bits");

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
        String predictions = getBadges().get("predictions");

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
        return Optional.ofNullable(getRawTag(tagName))
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
        return new EventChannel(getChannelId(), channelName);
    }

    /**
     * @return the channel name associated with this event, if applicable
     */
    public Optional<String> getChannelName() {
        return Optional.ofNullable(channelName);
    }

    /**
     * @return IRC message payload, which includes a colon prefix, unlike {@link #getMessage()}
     */
    public Optional<String> getPayload() {
        return Optional.ofNullable(payload).map(CharSequence::toString);
    }

    /**
     * @return the message sent by a user to an IRC channel
     */
    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    /**
     * @return the client name associated with the message, excluding the hostname
     */
    public Optional<String> getClientName() {
        return Optional.ofNullable(clientName)
            .filter(client -> !StringUtils.equals(client, "tmi.twitch.tv"))
            .filter(client -> !StringUtils.equals(client, "jtv"))
            .map(CharSequence::toString);
    }

    /**
     * @param name the tag key
     * @return the (not escaped) tag value associated with the specified key, in {@link CharSequence} form
     * @implNote This method is faster than {@link #getRawTagString(String)}
     * @apiNote This method is primarily intended for internal use by the library; please open an issue if a common tag is missing.
     */
    @Nullable
    @ApiStatus.Internal
    public CharSequence getRawTag(@NotNull String name) {
        return escapedTags.get(name);
    }

    /**
     * @param name the tag key
     * @return the (not escaped) tag value associated with the specified key, in {@link String} form
     * @implNote This method is slower than {@link #getRawTag(String)}
     * @apiNote This method is primarily intended for internal use by the library; please open an issue if a common tag is missing.
     */
    @Nullable
    @ApiStatus.Internal
    public String getRawTagString(@NotNull String name) {
        return Objects.toString(getRawTag(name), null);
    }

    /**
     * @return raw (i.e., not unescaped) IRCv3 tags
     * @deprecated in favor of {@link #getTagValue(String)} or {@link #getEscapedTags()}
     */
    @Deprecated
    public Map<String, String> getTags() {
        final Map<String, String> t = new HashMap<>(escapedTags.size() * 4 / 3 + 1);
        escapedTags.forEach((k, v) -> t.put(k, Objects.toString(v, null)));
        return Collections.unmodifiableMap(t);
    }

    /**
     * @return IRCv3 tags
     * @deprecated in favor of {@link #getEscapedTags()}
     */
    @Deprecated
    public Map<String, Object> getRawTags() {
        return Collections.unmodifiableMap(escapedTags);
    }

    private static String getEventId(Map<String, CharSequence> tags) {
        CharSequence id = tags.get("id");
        return id != null ? id.toString() : CryptoUtils.generateNonce(32);
    }

    private static Instant getEventTime(Map<String, CharSequence> tags) {
        CharSequence ts = tags.get("tmi-sent-ts");
        return ts != null ? Instant.ofEpochMilli(Long.parseLong(ts.toString())) : Instant.now();
    }

}
