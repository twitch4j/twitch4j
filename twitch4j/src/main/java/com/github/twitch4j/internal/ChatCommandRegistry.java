package com.github.twitch4j.internal;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.chat.events.channel.ListModsEvent;
import com.github.twitch4j.chat.events.channel.ListVipsEvent;
import com.github.twitch4j.common.enums.AnnouncementColor;
import com.github.twitch4j.common.events.domain.EventChannel;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.BanUserInput;
import com.github.twitch4j.helix.domain.ChannelVip;
import com.github.twitch4j.helix.domain.ChannelVipList;
import com.github.twitch4j.helix.domain.ChatSettings;
import com.github.twitch4j.helix.domain.Highlight;
import com.github.twitch4j.helix.domain.Moderator;
import com.github.twitch4j.helix.domain.ModeratorList;
import com.github.twitch4j.helix.domain.NamedUserChatColor;
import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.util.EnumUtil;
import com.github.twitch4j.util.PaginationUtil;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

@Slf4j
enum ChatCommandRegistry {
    INSTANCE;

    /**
     * Mapping of old chat names of common colors to their Helix equivalents.
     */
    private static final Map<String, String> HELIX_COLORS_BY_LOWER_CHAT_NAME = EnumUtil.buildMapping(NamedUserChatColor.values(), c -> c.toString().replace("_", ""), Enum::toString);

    /**
     * A cache of recent name => id queries.
     * <p>
     * Useful since chat typically operates on login names, while Helix operates on id's.
     */
    private static final Cache<String, String> USER_ID_BY_LOGIN_CACHE = CacheApi.create(spec -> {
        spec.expiryType(ExpiryType.POST_ACCESS);
        spec.expiryTime(Duration.ofMinutes(5L));
        spec.maxSize(2048L);
    });

    @Getter
    private final Map<String, ChatCommandHandler> commandHandlers;

    ChatCommandRegistry() {
        final Map<String, ChatCommandHandler> m = new HashMap<>();

        BiConsumer<ChatCommandHelixForwarder.CommandArguments, AnnouncementColor> announceHandler = (args, color) -> {
            if (args.getRestOfMessage() == null || args.getRestOfMessage().isEmpty()) return;
            args.getHelix().sendChatAnnouncement(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), args.getRestOfMessage(), color).execute();
        };
        m.put("announce", args -> announceHandler.accept(args, AnnouncementColor.PRIMARY));
        m.put("announceblue", args -> announceHandler.accept(args, AnnouncementColor.BLUE));
        m.put("announcegreen", args -> announceHandler.accept(args, AnnouncementColor.GREEN));
        m.put("announceorange", args -> announceHandler.accept(args, AnnouncementColor.ORANGE));
        m.put("announcepurple", args -> announceHandler.accept(args, AnnouncementColor.PURPLE));

        m.put("ban", args -> {
            String[] banParts = StringUtils.split(args.getRestOfMessage(), " ", 2);
            if (banParts.length == 0) return;
            String banReason = banParts.length > 1 ? banParts[1] : "";
            doBan(args.getHelix(), args.getToken(), args.getChannelId(), getId(args.getHelix(), args.getToken(), banParts[0], null), banReason, null);
        });

        m.put("color", new ChatCommandHandler() {
            @Override
            public void accept(ChatCommandHelixForwarder.CommandArguments args) {
                String color = args.getRestOfMessage().startsWith("#") ? args.getRestOfMessage() : HELIX_COLORS_BY_LOWER_CHAT_NAME.get(args.getRestOfMessage().toLowerCase());
                if (color == null) return;
                args.getHelix().updateUserChatColor(args.getToken().getAccessToken(), args.getToken().getUserId(), color).execute();
            }

            @Override
            public ChatCommandRateLimitType getLimitKey() {
                return ChatCommandRateLimitType.USER; // this helix call is associated with a user's chat color, irrespective of channel
            }
        });

        m.put("commercial", args -> {
            if (args.getRestOfMessage() == null || args.getRestOfMessage().isEmpty()) return;
            int length = Integer.parseInt(args.getRestOfMessage());
            length = (length / 30) * 30; // require a multiple of 30 seconds
            length = Math.max(Math.min(length, 180), 30); // clamp to [30, 180]
            args.getHelix().startCommercial(args.getToken().getAccessToken(), args.getChannelId(), length).execute();
        });

        BiConsumer<ChatCommandHelixForwarder.CommandArguments, String> deleteHandler = (args, messageId) -> {
            if (messageId != null && messageId.isEmpty()) return;
            args.getHelix().deleteChatMessages(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), messageId).execute();
        };
        m.put("clear", args -> deleteHandler.accept(args, null));
        m.put("delete", args -> deleteHandler.accept(args, args.getRestOfMessage()));

        ChatCommandHandler unbanHandler = args -> {
            String userId = getId(args.getHelix(), args.getToken(), args.getRestOfMessage(), null);
            args.getHelix().unbanUser(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), userId).execute();
        };
        m.put("unban", unbanHandler);
        m.put("untimeout", unbanHandler);

        m.put("marker", args -> {
            String description = args.getRestOfMessage() != null ? args.getRestOfMessage() : "";
            args.getHelix().createStreamMarker(args.getToken().getAccessToken(), new Highlight(args.getChannelId(), description)).execute();
        });

        m.put("mod", args -> {
            String targetName = args.getRestOfMessage();
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName.toLowerCase()));
            args.getHelix().addChannelModerator(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("unmod", args -> {
            String targetName = args.getRestOfMessage();
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName.toLowerCase()));
            args.getHelix().removeChannelModerator(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("mods", args -> {
            String channelId = args.getChannelId();
            if (args.getChannelName() == null || channelId == null) return;

            AtomicBoolean failure = new AtomicBoolean();
            List<Moderator> mods = PaginationUtil.getPaginated(
                cursor -> {
                    try {
                        return args.getHelix().getModerators(args.getToken().getAccessToken(), channelId, null, cursor, 100).execute();
                    } catch (Exception e) {
                        failure.set(true);
                        getLogger().warn("Failed to query moderators from Helix chat command forwarder", e);
                        return null;
                    }
                },
                ModeratorList::getModerators,
                result -> result.getPagination() != null ? result.getPagination().getCursor() : null
            );
            if (failure.get()) return;

            List<String> names = new ArrayList<>(mods.size());
            mods.forEach(v -> names.add(v.getUserLogin()));

            EventChannel channel = new EventChannel(channelId, args.getChannelName());
            ListModsEvent event = new ListModsEvent(channel, names);
            args.getChat().getEventManager().publish(event);
        });

        m.put("raid", args -> {
            String targetName = args.getRestOfMessage();
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName.toLowerCase()));
            args.getHelix().startRaid(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("unraid", args -> args.getHelix().cancelRaid(args.getToken().getAccessToken(), args.getChannelId()).execute());

        BiConsumer<ChatCommandHelixForwarder.CommandArguments, ChatSettings> roomHandler = (args, settings) -> {
            if (settings == null) return;
            args.getHelix().updateChatSettings(args.getToken().getAccessToken(), args.getChannelId(), args.getToken().getUserId(), settings).execute();
        };
        m.put("emoteonly", args -> roomHandler.accept(args, ChatSettings.builder().isEmoteOnlyMode(true).build()));
        m.put("emoteonlyoff", args -> roomHandler.accept(args, ChatSettings.builder().isEmoteOnlyMode(false).build()));
        m.put("followers", args -> {
            Integer followTime = parseDuration(args.getRestOfMessage());
            followTime = followTime != null ? followTime : 0; // default is 0
            followTime = Math.min(Math.max(followTime, 0), 129600); // clamp to [0, 129600]
            roomHandler.accept(
                args,
                ChatSettings.builder()
                    .isFollowersOnlyMode(true)
                    .followerModeDuration(followTime)
                    .build()
            );
        });
        m.put("followersoff", args -> roomHandler.accept(args, ChatSettings.builder().isFollowersOnlyMode(false).build()));
        m.put("slow", args -> {
            Integer slowTime = parseDuration(args.getRestOfMessage());
            slowTime = slowTime != null ? slowTime : 30; // default is 30 seconds
            slowTime = Math.min(Math.max(slowTime, 3), 120); // clamp to [3, 120]
            roomHandler.accept(
                args,
                ChatSettings.builder()
                    .isSlowMode(true)
                    .slowModeWaitTime(slowTime)
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
            String[] timeoutParts = StringUtils.split(args.getRestOfMessage(), " ", 3);
            if (timeoutParts.length < 2) return;
            String timeoutReason = timeoutParts.length >= 3 ? timeoutParts[2] : "";
            Integer seconds = parseDuration(timeoutParts[1]);
            if (seconds == null) return;
            seconds = Math.min(Math.max(seconds, 1), 1209600); // clamp to [1, 1_209_600]
            doBan(args.getHelix(), args.getToken(), args.getChannelId(), getId(args.getHelix(), args.getToken(), timeoutParts[0], null), timeoutReason, seconds);
        });

        m.put("vip", args -> {
            String targetName = args.getRestOfMessage();
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName.toLowerCase()));
            args.getHelix().addChannelVip(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("unvip", args -> {
            String targetName = args.getRestOfMessage();
            if (targetName == null || targetName.isEmpty()) return;
            String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName.toLowerCase()));
            args.getHelix().removeChannelVip(args.getToken().getAccessToken(), args.getChannelId(), targetId).execute();
        });

        m.put("vips", args -> {
            String channelId = args.getChannelId();
            if (args.getChannelName() == null || channelId == null) return;

            AtomicBoolean failure = new AtomicBoolean();
            List<ChannelVip> vips = PaginationUtil.getPaginated(
                cursor -> {
                    try {
                        return args.getHelix().getChannelVips(args.getToken().getAccessToken(), channelId, null, 100, cursor).execute();
                    } catch (Exception e) {
                        failure.set(true);
                        getLogger().warn("Failed to query VIPs from Helix chat command forwarder", e);
                        return null;
                    }
                },
                ChannelVipList::getData,
                result -> result.getPagination() != null ? result.getPagination().getCursor() : null
            );
            if (failure.get()) return;

            List<String> names = new ArrayList<>(vips.size());
            vips.forEach(v -> names.add(v.getUserLogin()));

            EventChannel channel = new EventChannel(channelId, args.getChannelName());
            ListVipsEvent event = new ListVipsEvent(channel, names);
            args.getChat().getEventManager().publish(event);
        });

        m.put("w", new ChatCommandHandler() {
            @Override
            public void accept(ChatCommandHelixForwarder.CommandArguments args) {
                String[] whisperParts = StringUtils.split(args.getRestOfMessage(), " ", 2);
                if (whisperParts.length != 2) return;

                String message = whisperParts[1];
                if (message == null || message.isEmpty()) return;

                String targetName = whisperParts[0];
                String targetId = getId(args.getHelix(), args.getToken(), targetName, args.getChat().getChannelNameToChannelId().get(targetName.toLowerCase()));

                args.getHelix().sendWhisper(args.getToken().getAccessToken(), args.getToken().getUserId(), targetId, message).execute();
            }

            @Override
            public ChatCommandRateLimitType getLimitKey() {
                return ChatCommandRateLimitType.USER; // whisper sender matters more than the channel for rate limiting
            }
        });

        this.commandHandlers = Collections.unmodifiableMap(m);
    }

    private Logger getLogger() {
        return log;
    }

    /**
     * Obtains the user id associated with the passed username.
     * <p>
     * Before executing a helix users call, this method checks for a match in the token or final id argument.
     *
     * @param helix        the api instance to perform a getUsers call, if necessary
     * @param token        a user access token that may or may not be associated with the passed username
     * @param name         the name of the user whose id is being queried
     * @param optimisticId the id of the user, if already known, or null
     * @return the user id
     * @throws com.netflix.hystrix.exception.HystrixRuntimeException if the helix call fails
     * @throws IndexOutOfBoundsException                             if the user was not found from an otherwise successful helix call
     */
    static String getId(TwitchHelix helix, OAuth2Credential token, @NotNull String name, @Nullable String optimisticId) {
        // Check optimistic id sources
        if (optimisticId != null) return optimisticId;
        if (name.equalsIgnoreCase(token.getUserName())) return token.getUserId();

        // Check id cache
        String cachedId = USER_ID_BY_LOGIN_CACHE.get(name.toLowerCase());
        if (cachedId != null) return cachedId;

        // Fallback to querying via helix
        User user = helix.getUsers(token.getAccessToken(), null, Collections.singletonList(name)).execute().getUsers().get(0);
        USER_ID_BY_LOGIN_CACHE.put(user.getLogin(), user.getId());
        return user.getId();
    }

    /**
     * Performs a ban or timeout via helix.
     *
     * @param helix     the api instance to perform the call
     * @param token     a user access token representing a moderator in the channel
     * @param channelId the id of the channel from which the user should be banned
     * @param targetId  the id of the user who should be banned
     * @param reason    the reason of the ban to be displayed to the user and other moderators
     * @param duration  the duration of the timeout, or null for a ban
     * @throws com.netflix.hystrix.exception.HystrixRuntimeException if the helix call fails
     */
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

    /**
     * Parses a time string into the corresponding number of seconds.
     * <p>
     * Supports weeks, days, hours, minutes, seconds.
     * Time units can be abbreviated or completely written out.
     * Spaces and (grammatically correct) commas are ignored.
     *
     * @param time the user-inputted time duration string
     * @return the seconds corresponding to the string time duration, or null if parsing failed
     */
    @Nullable
    static Integer parseDuration(@NotNull String time) {
        int seconds = 0;

        int part = 0;
        for (int i = 0, n = time.length(); i < n; i++) {
            char c = time.charAt(i);
            if (Character.isDigit(c)) {
                part *= 10;
                part += c - '0';
            } else {
                if (i == 0 && c != ' ')
                    return null; // invalid format

                switch (Character.toLowerCase(c)) {
                    case 's':
                        seconds += part;
                        break;
                    case 'm':
                        if (i + 1 < n && Character.toLowerCase(time.charAt(i + 1)) == 'o') {
                            seconds += part * 60 * 60 * 24 * 7 * 4; // months
                        } else {
                            seconds += part * 60; // minutes
                        }
                        break;
                    case 'h':
                        seconds += part * 60 * 60; // hours
                        break;
                    case 'd':
                        seconds += part * 60 * 60 * 24; // days
                        break;
                    case 'w':
                        seconds += part * 60 * 60 * 24 * 7; // weeks
                        break;
                    default:
                        if (part > 0) {
                            if (c == ' ') continue;
                            return null; // failed to parse
                        }
                        break; // ignore random letters unassociated with a number
                }
                part = 0;
            }
        }
        seconds += part;

        return seconds;
    }
}
