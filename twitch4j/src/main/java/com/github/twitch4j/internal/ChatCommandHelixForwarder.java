package com.github.twitch4j.internal;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.util.TwitchChatLimitHelper;
import com.github.twitch4j.common.util.BucketUtils;
import com.github.twitch4j.helix.TwitchHelix;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiPredicate;

/**
 * Intercepts and forwards outbound IRC messages containing commands to the Helix API, when able.
 * <p>
 * Allows for a smoother transition when chat commands can no longer be sent over IRC;
 * users simply just need to add the latest scopes to their user access token.
 * <p>
 * This class is marked as internal, and could face breaking changes at any time.
 *
 * @see <a href="https://discuss.dev.twitch.tv/t/deprecation-of-chat-commands-through-irc/40486">Announcement for the deprecation of most chat commands</a>
 * @see com.github.twitch4j.TwitchClientBuilder#withChatCommandsViaHelix(boolean)
 * @see #test(TwitchChat, String)
 */
@Slf4j
@ApiStatus.Internal
public final class ChatCommandHelixForwarder implements BiPredicate<TwitchChat, String> {

    /**
     * The forwarder will be enabled by default on 2023-02-21 (shortly before the 24-hour brown-out period, and three days before complete shutdown).
     */
    public static final Instant ENABLE_AFTER = Instant.ofEpochSecond(1677000000L);

    /**
     * Identifies outbound raw irc messages that contain chat commands.
     */
    static final Pattern COMMAND_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?PRIVMSG\\s#(?<channel>\\S*?)\\s:\\s*[/\\.](?<command>.+)$");

    /**
     * A registry of handlers of each chat command to be forwarded to Helix.
     */
    private static final Map<String, ChatCommandHandler> COMMAND_HANDLERS = ChatCommandRegistry.INSTANCE.getCommandHandlers();

    /**
     * The Helix instance for executing API calls.
     */
    private final TwitchHelix helix;

    /**
     * The user access token to be specified in API calls.
     */
    private final OAuth2Credential token;

    /**
     * The scheduled executor service for performing api calls asynchronously.
     */
    private final ScheduledExecutorService executor;

    /**
     * The per channel helix bandwidth.
     */
    private final Bandwidth channelHelixLimit;

    /**
     * The cached helix rate limit bucket for each channel.
     */
    private final Cache<String, Bucket> bucketByChannel;

    /**
     * Constructs a forwarder of chat commands over IRC to Helix.
     *
     * @param helix             the helix instance to execute api calls
     * @param token             the user access token to be specified in api calls
     * @param identityProvider  the twitch identity provider to query token characteristics
     * @param executor          the scheduled executor service for performing api calls asynchronously
     * @param channelHelixLimit the per channel helix rate limit
     */
    public ChatCommandHelixForwarder(TwitchHelix helix, OAuth2Credential token, TwitchIdentityProvider identityProvider, ScheduledExecutorService executor, Bandwidth channelHelixLimit) {
        this.helix = helix;
        this.token = token;
        this.executor = executor;
        this.channelHelixLimit = channelHelixLimit != null ? channelHelixLimit : TwitchChatLimitHelper.MOD_MESSAGE_LIMIT;
        this.bucketByChannel = CacheApi.create(spec -> {
            spec.expiryType(ExpiryType.POST_ACCESS);
            spec.expiryTime(Duration.ofNanos(this.channelHelixLimit.getRefillPeriodNanos() * 2));
            spec.maxSize(2048L);
        });
        if (token != null && StringUtils.isEmpty(token.getUserId())) {
            TwitchIdentityProvider tip = identityProvider != null ? identityProvider : new TwitchIdentityProvider(null, null, null);
            tip.getAdditionalCredentialInformation(token).ifPresent(token::updateCredential);
            if (StringUtils.isEmpty(token.getUserId())) log.warn("ChatCommandHelixForwarder requires a valid user access token to function correctly.");
        }
        log.debug("Initialized ChatCommandHelixForwarder");
    }

    /**
     * {@inheritDoc}
     *
     * @param chat          the chat instance with the outbound message call
     * @param rawIrcCommand the raw irc command to be sent to the server, if not intercepted
     * @return whether the irc command was forwarded to helix, indicating it does not need to be sent via the chat socket
     */
    @Override
    public boolean test(TwitchChat chat, String rawIrcCommand) {
        if (helix == null || token == null) return false;

        final Matcher matcher = COMMAND_PATTERN.matcher(rawIrcCommand);
        if (matcher.matches()) {
            // Get channel info
            String channelName = matcher.group("channel");

            // Get command info
            String fullMessage = matcher.group("command").trim();
            int firstSpace = fullMessage.indexOf(' ');
            String command = fullMessage.substring(0, firstSpace > 0 ? firstSpace : fullMessage.length());
            String restOfMessage = firstSpace > 0 ? fullMessage.substring(firstSpace + 1) : "";

            // Execute command handler
            ChatCommandHandler handler = COMMAND_HANDLERS.get(command.toLowerCase());
            if (handler != null) {
                log.trace("Handling chat command from Helix forwarder: /" + fullMessage);
                BucketUtils.scheduleAgainstBucket(getCommandBucket(handler.getLimitKey(), channelName), executor, () -> {
                    try {
                        handler.accept(new CommandArguments(chat, channelName, command, restOfMessage));
                    } catch (Exception e) {
                        log.warn("Failed to execute command from Helix forwarder: /" + fullMessage, e);
                    }
                });
                return true;
            }
        }

        return false; // false => don't block the command from sending
    }

    /**
     * Obtains the rate limit bucket for executing a command for a particular channel or user.
     *
     * @param limitType   the relevant key for the rate limit of this command
     * @param channelName the login name of the channel where the command is being sent to (irrelevant for USER limit type)
     * @return Bucket
     */
    private Bucket getCommandBucket(ChatCommandRateLimitType limitType, String channelName) {
        String key = limitType == ChatCommandRateLimitType.CHANNEL ? channelName.toLowerCase() : ""; // empty string represents current user in token
        return bucketByChannel.computeIfAbsent(key, k -> BucketUtils.createBucket(channelHelixLimit));
    }

    /**
     * The parsed details from a raw irc message associated with a command to be forwarded to helix.
     */
    @Value
    class CommandArguments {
        /**
         * The chat instance where the command took place.
         */
        TwitchChat chat;

        /**
         * The channel name where the command was to be sent.
         */
        String channelName;

        /**
         * The command name, without a forward slash or any following arguments.
         */
        String command;

        /**
         * The remainder of the trimmed message following the command name.
         */
        String restOfMessage;

        /**
         * @return the id of the channel associated with {@link #getChannelName()}.
         */
        String getChannelId() {
            return ChatCommandRegistry.getId(helix, token, channelName, chat.getChannelNameToChannelId().get(channelName.toLowerCase()));
        }

        /**
         * @return the helix instance to be used for api calls.
         */
        TwitchHelix getHelix() {
            return helix;
        }

        /**
         * @return the (user) access token to be used for api calls.
         */
        OAuth2Credential getToken() {
            return token;
        }
    }
}
