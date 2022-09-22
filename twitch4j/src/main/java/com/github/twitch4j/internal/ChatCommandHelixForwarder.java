package com.github.twitch4j.internal;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.chat.events.channel.ListModsEvent;
import com.github.twitch4j.chat.events.channel.ListVipsEvent;
import com.github.twitch4j.chat.util.TwitchChatLimitHelper;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.common.util.BucketUtils;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.AnnouncementColor;
import com.github.twitch4j.helix.domain.BanUserInput;
import com.github.twitch4j.helix.domain.ChannelVip;
import com.github.twitch4j.helix.domain.ChannelVipList;
import com.github.twitch4j.helix.domain.ChatSettings;
import com.github.twitch4j.helix.domain.Highlight;
import com.github.twitch4j.helix.domain.Moderator;
import com.github.twitch4j.helix.domain.ModeratorList;
import com.github.twitch4j.helix.domain.NamedUserChatColor;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.util.PaginationUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public final class ChatCommandHelixForwarder implements BiPredicate<TwitchChat, String> {
    private static final Pattern COMMAND_PATTERN;
    private static final Map<String, String> HELIX_COLORS_BY_LOWER_CHAT_NAME;
    private static final Map<String, CommandHandler> COMMAND_HANDLERS;
    private static final Cache<String, String> USER_ID_BY_LOGIN_CACHE;
    private final TwitchHelix helix;
    private final OAuth2Credential token;
    private final ScheduledExecutorService executor;
    private final Bandwidth channelHelixLimit;
    private final Cache<String, Bucket> bucketByChannel;

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
        if (StringUtils.isEmpty(token.getUserId())) {
            TwitchIdentityProvider tip = identityProvider != null ? identityProvider : new TwitchIdentityProvider(null, null, null);
            tip.getAdditionalCredentialInformation(token).ifPresent(token::updateCredential);
        }
        log.debug("Initialized ChatCommandHelixForwarder");
    }

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
            CommandHandler handler = COMMAND_HANDLERS.get(command.toLowerCase());
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
    private Bucket getCommandBucket(CommandRateLimitType limitType, String channelName) {
        String key = limitType == CommandRateLimitType.CHANNEL ? channelName.toLowerCase() : ""; // empty string represents current user in token
        return bucketByChannel.computeIfAbsent(key, k -> BucketUtils.createBucket(channelHelixLimit));
    }

    private static String getId(TwitchHelix helix, OAuth2Credential token, @NotNull String name, @Nullable String optimisticId) {
        if (optimisticId != null) return optimisticId;
        if (name.equalsIgnoreCase(token.getUserName())) return token.getUserId();

        String cachedId = USER_ID_BY_LOGIN_CACHE.get(name.toLowerCase());
        if (cachedId != null) return cachedId;

        User user = helix.getUsers(token.getAccessToken(), null, Collections.singletonList(name)).execute().getUsers().get(0);
        USER_ID_BY_LOGIN_CACHE.put(user.getLogin(), user.getId());
        return user.getId();
    }

    private static void doBan(TwitchHelix helix, OAuth2Credential token, String channelId, String targetId, String reason, Integer duration) {
        if (channelId == null || targetId == null || (duration != null && duration == 0)) return;
        helix.banUser(
            token.getAccessToken(),
            channelId,
            token.getUserId(),
            BanUserInput.builder()
                .userId(targetId)
                .reason(reason != null ? reason : "")
                .duration(duration)
                .build()
        ).execute();
    }

    private static Integer parseDuration(String time) {
        int seconds = 0;

        int part = 0;
        for (int i = 0, n = time.length(); i < n; i++) {
            char c = time.charAt(i);
            if (Character.isDigit(c)) {
                part *= 10;
                part += c - '0';
            } else {
                if (i == 0)
                    return null; // invalid format

                switch (c) {
                    case 's':
                        seconds += part;
                        break;
                    case 'm':
                        seconds += part * 60;
                        break;
                    case 'h':
                        seconds += part * 60 * 60;
                        break;
                    case 'd':
                        seconds += part * 60 * 60 * 24;
                        break;
                    case 'w':
                        seconds += part * 60 * 60 * 24 * 7;
                        break;
                    default:
                        if (part > 0) {
                            if (c == ' ') continue;
                            return null; // failed to parse
                        }
                        break;
                }
                part = 0;
            }
        }
        seconds += part;

        return seconds;
    }

    @Value
    private class CommandArguments {
        TwitchChat chat;
        String channelName;
        String command;
        String restOfMessage;

        String getChannelId() {
            return getId(helix, token, channelName, chat.getChannelNameToChannelId().get(channelName));
        }

        TwitchHelix getHelix() {
            return helix;
        }

        OAuth2Credential getToken() {
            return token;
        }
    }

    /**
     * The primary key that is associated with the rate limit bucket for this (helix) command.
     */
    private enum CommandRateLimitType {
        /**
         * The command should be rate limited based on which channel it was executed in.
         */
        CHANNEL,

        /**
         * The command should be rate limited based on which user executed it.
         * <p>
         * As {@code token} is final, this assumed to be a single user.
         */
        USER
    }

    /**
     * A handler of a specific command type; consumes arguments from the chat message.
     */
    @FunctionalInterface
    private interface CommandHandler extends Consumer<CommandArguments> {
        default CommandRateLimitType getLimitKey() {
            return CommandRateLimitType.CHANNEL; // most commands should be rate limited based on which channel they were executed in
        }
    }

    static {
        COMMAND_PATTERN = Pattern.compile("^(?:@(?<tags>\\S+?)\\s)?PRIVMSG\\s#(?<channel>\\S*?)\\s:/(?<command>.+)$");

        HELIX_COLORS_BY_LOWER_CHAT_NAME = Arrays.stream(NamedUserChatColor.values())
            .collect(Collectors.toMap(c -> c.toString().replace("_", ""), NamedUserChatColor::toString));

        USER_ID_BY_LOGIN_CACHE = CacheApi.create(spec -> {
            spec.expiryType(ExpiryType.POST_ACCESS);
            spec.expiryTime(Duration.ofMinutes(5L));
            spec.maxSize(2048L);
        });

        final Map<String, CommandHandler> m = new HashMap<>();

        BiConsumer<CommandArguments, AnnouncementColor> announceHandler = (args, color) -> {
            if (args.restOfMessage == null || args.restOfMessage.isEmpty()) return;
            args.getHelix().sendChatAnnouncement(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), args.restOfMessage, color).execute();
        };
        m.put("announce", args -> announceHandler.accept(args, AnnouncementColor.PRIMARY));
        m.put("announceblue", args -> announceHandler.accept(args, AnnouncementColor.BLUE));
        m.put("announcegreen", args -> announceHandler.accept(args, AnnouncementColor.GREEN));
        m.put("announceorange", args -> announceHandler.accept(args, AnnouncementColor.ORANGE));
        m.put("announcepurple", args -> announceHandler.accept(args, AnnouncementColor.PURPLE));

        m.put("ban", args -> {
            String[] banParts = StringUtils.split(args.restOfMessage, " ", 2);
            if (banParts.length == 0) return;
            String banReason = banParts.length > 1 ? banParts[1] : "";
            doBan(args.getHelix(), args.getToken(), args.getChannelId(), getId(args.getHelix(), args.getToken(), banParts[0], null), banReason, null);
        });

        m.put("color", new CommandHandler() {
            @Override
            public void accept(CommandArguments args) {
                String color = args.restOfMessage.startsWith("#") ? args.restOfMessage : HELIX_COLORS_BY_LOWER_CHAT_NAME.get(args.restOfMessage.toLowerCase());
                if (color == null) return;
                args.getHelix().updateUserChatColor(args.getToken().getAccessToken(), args.getToken().getUserId(), color).execute();
            }

            @Override
            public CommandRateLimitType getLimitKey() {
                return CommandRateLimitType.USER; // this helix call is associated with a user's chat color, irrespective of channel
            }
        });

        m.put("commercial", args -> {
            if (args.restOfMessage == null || args.restOfMessage.isEmpty()) return;
            args.getHelix().startCommercial(args.getToken().getAccessToken(), args.getChannelId(), Integer.parseInt(args.restOfMessage)).execute();
        });

        BiConsumer<CommandArguments, String> deleteHandler = (args, messageId) -> {
            if (messageId != null && messageId.isEmpty()) return;
            args.getHelix().deleteChatMessages(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), messageId).execute();
        };
        m.put("clear", args -> deleteHandler.accept(args, null));
        m.put("delete", args -> deleteHandler.accept(args, args.restOfMessage));

        CommandHandler unbanHandler = args -> {
            String userId = getId(args.getHelix(), args.getToken(), args.restOfMessage, null);
            args.getHelix().unbanUser(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), userId).execute();
        };
        m.put("unban", unbanHandler);
        m.put("untimeout", unbanHandler);

        m.put("marker", args -> {
            String description = args.restOfMessage != null ? args.restOfMessage : "";
            args.getHelix().createStreamMarker(args.getToken().getAccessToken(), new Highlight(args.getChannelId(), description)).execute();
        });

        m.put("mod", args -> {
            String targetName = args.restOfMessage;
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName));
            args.getHelix().addChannelModerator(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("unmod", args -> {
            String targetName = args.restOfMessage;
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName));
            args.getHelix().removeChannelModerator(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("mods", args -> {
            String channelId = args.getChannelId();
            if (args.channelName == null || channelId == null) return;

            List<Moderator> mods = PaginationUtil.getPaginated(
                cursor -> {
                    try {
                        return args.getHelix().getModerators(args.getToken().getAccessToken(), channelId, null, cursor, 100).execute();
                    } catch (Exception e) {
                        return null;
                    }
                },
                ModeratorList::getModerators,
                result -> result.getPagination() != null ? result.getPagination().getCursor() : null
            );
            List<String> names = new ArrayList<>(mods.size());
            mods.forEach(v -> names.add(v.getUserLogin()));

            EventChannel channel = new EventChannel(channelId, args.channelName);
            ListModsEvent event = new ListModsEvent(channel, names);
            args.getChat().getEventManager().publish(event);
        });

        m.put("raid", args -> {
            String targetName = args.restOfMessage;
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName));
            args.getHelix().startRaid(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("unraid", args -> args.getHelix().cancelRaid(args.getToken().getAccessToken(), args.getChannelId()).execute());

        BiConsumer<CommandArguments, ChatSettings> roomHandler = (args, settings) -> {
            if (settings == null) return;
            args.getHelix().updateChatSettings(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), settings).execute();
        };
        m.put("emoteonly", args -> roomHandler.accept(args, ChatSettings.builder().isEmoteOnlyMode(true).build()));
        m.put("emoteonlyoff", args -> roomHandler.accept(args, ChatSettings.builder().isEmoteOnlyMode(false).build()));
        m.put("followers", args -> {
            Integer followTime = parseDuration(args.restOfMessage);
            roomHandler.accept(
                args,
                ChatSettings.builder()
                    .isFollowersOnlyMode(true)
                    .followerModeDuration(followTime != null ? followTime : 0)
                    .build()
            );
        });
        m.put("followersoff", args -> roomHandler.accept(args, ChatSettings.builder().isFollowersOnlyMode(false).build()));
        m.put("slow", args -> {
            Integer slowTime = parseDuration(args.restOfMessage);
            roomHandler.accept(
                args,
                ChatSettings.builder()
                    .isSlowMode(true)
                    .slowModeWaitTime(slowTime != null && slowTime > 0 ? slowTime : 30)
                    .build()
            );
        });
        m.put("slowoff", args -> roomHandler.accept(args, ChatSettings.builder().isSlowMode(false).build()));
        m.put("subscribers", args -> roomHandler.accept(args, ChatSettings.builder().isSubscribersOnlyMode(true).build()));
        m.put("subscribersoff", args -> roomHandler.accept(args, ChatSettings.builder().isSubscribersOnlyMode(false).build()));
        m.put("r9kbeta", args -> roomHandler.accept(args, ChatSettings.builder().isUniqueChatMode(true).build()));
        m.put("uniquechat", args -> roomHandler.accept(args, ChatSettings.builder().isUniqueChatMode(true).build()));
        m.put("r9kbetaoff", args -> roomHandler.accept(args, ChatSettings.builder().isUniqueChatMode(false).build()));
        m.put("uniquechatoff", args -> roomHandler.accept(args, ChatSettings.builder().isUniqueChatMode(false).build()));

        m.put("timeout", args -> {
            String[] timeoutParts = StringUtils.split(args.restOfMessage, " ", 3);
            if (timeoutParts.length < 2) return;
            String timeoutReason = timeoutParts.length >= 3 ? timeoutParts[2] : "";
            Integer seconds = parseDuration(timeoutParts[1]);
            if (seconds != null)
                doBan(args.getHelix(), args.getToken(), args.getChannelId(), getId(args.getHelix(), args.getToken(), timeoutParts[0], null), timeoutReason, seconds);
        });

        m.put("vip", args -> {
            String targetName = args.restOfMessage;
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName));
            args.getHelix().addChannelVip(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("unvip", args -> {
            String targetName = args.restOfMessage;
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName));
            args.getHelix().removeChannelVip(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("vips", args -> {
            String channelId = args.getChannelId();
            if (args.channelName == null || channelId == null) return;

            List<ChannelVip> vips = PaginationUtil.getPaginated(
                cursor -> {
                    try {
                        return args.getHelix().getChannelVips(args.getToken().getAccessToken(), channelId, null, 100, cursor).execute();
                    } catch (Exception e) {
                        return null;
                    }
                },
                ChannelVipList::getData,
                result -> result.getPagination() != null ? result.getPagination().getCursor() : null
            );
            List<String> names = new ArrayList<>(vips.size());
            vips.forEach(v -> names.add(v.getUserLogin()));

            EventChannel channel = new EventChannel(channelId, args.channelName);
            ListVipsEvent event = new ListVipsEvent(channel, names);
            args.getChat().getEventManager().publish(event);
        });

        m.put("w", new CommandHandler() {
            @Override
            public void accept(CommandArguments args) {
                String[] whisperParts = StringUtils.split(args.restOfMessage, " ", 2);
                if (whisperParts.length != 2) return;

                String message = whisperParts[1];
                if (message == null || message.isEmpty()) return;

                String targetName = whisperParts[0];
                String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName));

                args.getHelix().sendWhisper(args.getToken().getAccessToken(), args.getToken().getUserId(), targetId, message).execute();
            }

            @Override
            public CommandRateLimitType getLimitKey() {
                return CommandRateLimitType.USER; // whisper sender matters more than the channel for rate limiting
            }
        });

        COMMAND_HANDLERS = Collections.unmodifiableMap(m);
    }
}
