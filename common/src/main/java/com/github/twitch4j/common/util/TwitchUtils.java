package com.github.twitch4j.common.util;

import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TwitchUtils {

    /**
     * The account used by twitch to signify an anonymous subscription gifter
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/anonymous-sub-gifting-to-launch-11-15-launch-details/18683">Official Announcement</a>
     */
    public static final EventUser ANONYMOUS_GIFTER = new EventUser("274598607", "ananonymousgifter");

    /**
     * The account used by twitch to signify an anonymous cheerer
     */
    public static final EventUser ANONYMOUS_CHEERER = new EventUser("407665396", "ananonymouscheerer");

    @Deprecated // not used by twitch4j
    public static Set<CommandPermission> getPermissionsFromTags(Map<String, Object> tags) {
        return getPermissionsFromTags(tags, new HashMap<>());
    }

    @Deprecated // not used by twitch4j
    public static Set<CommandPermission> getPermissionsFromTags(@NonNull Map<String, Object> tags, @NonNull Map<String, String> badges) {
        return getPermissionsFromTags(tags, badges, null, null);
    }

    @ApiStatus.Internal
    public static Set<CommandPermission> getPermissionsFromTags(@NonNull Map<String, Object> tags, @NonNull Map<String, String> badges, String userId, Collection<String> botOwnerIds) {
        // allows for accurate sub detection when user has the founder badge equipped
        Object subscriber = tags.get("subscriber");
        if (subscriber instanceof CharSequence && !StringUtils.equals("0", (CharSequence) subscriber)) {
            badges.put("subscriber", subscriber.toString());
        }

        // irc parsing branch
        Object inputBadges = tags.get("badges");
        if (inputBadges instanceof CharSequence) {
            return getPermissionsFromTags((CharSequence) inputBadges, userId, botOwnerIds, badges);
        }

        // otherwise: handle pubsub whispers topic
        if (inputBadges instanceof Collection) {
            Collection<?> list = (Collection<?>) inputBadges;
            for (Object badgeObj : list) {
                if (badgeObj instanceof Map) {
                    Map<?, ?> badge = (Map<?, ?>) badgeObj;
                    Object badgeId = badge.get("id");
                    if (badgeId instanceof String) {
                        Object badgeVersion = badge.get("version");
                        badges.put((String) badgeId, badgeVersion instanceof String ? (String) badgeVersion : null);
                    }
                }
            }
        }
        return getPermissionsFromTags(null, userId, botOwnerIds, badges);
    }

    @ApiStatus.Internal
    public static <T> Set<CommandPermission> getPermissions(Iterable<T> badges, Function<T, String> badgeName, Function<T, String> badgeValue) {
        Set<CommandPermission> perms = EnumSet.of(CommandPermission.EVERYONE);
        for (T badge : badges) {
            String key = badgeName.apply(badge);
            switch (key) {
                case "premium":
                case "turbo":
                    perms.add(CommandPermission.PRIME_TURBO);
                    break;

                case "partner":
                case "ambassador":
                    perms.add(CommandPermission.PARTNER);
                    break;

                case "subscriber":
                    perms.add(CommandPermission.SUBSCRIBER);
                    break;

                case "founder":
                    // note: value contains tier
                    perms.add(CommandPermission.FOUNDER);
                    break;

                case "sub-gifter":
                case "sub-gift-leader":
                    perms.add(CommandPermission.SUBGIFTER);
                    break;

                case "bits":
                case "bits-leader":
                case "anonymous-cheerer":
                    perms.add(CommandPermission.BITS_CHEERER);
                    break;

                case "hype-train":
                    String hypeBadge = badgeValue.apply(badge);
                    if ("1".equals(hypeBadge)) {
                        perms.add(CommandPermission.CURRENT_HYPE_TRAIN_CONDUCTOR);
                    } else if ("2".equals(hypeBadge)) {
                        perms.add(CommandPermission.FORMER_HYPE_TRAIN_CONDUCTOR);
                    }
                    break;

                case "predictions":
                    String predictionBadge = badgeValue.apply(badge);
                    if (predictionBadge != null && !predictionBadge.isEmpty()) {
                        char first = predictionBadge.charAt(0);
                        if (first == 'b') {
                            perms.add(CommandPermission.PREDICTIONS_BLUE);
                        } else if (first == 'p') {
                            perms.add(CommandPermission.PREDICTIONS_PINK);
                        }
                    }
                    break;

                case "no_audio":
                    perms.add(CommandPermission.NO_AUDIO);
                    break;

                case "no_video":
                    perms.add(CommandPermission.NO_VIDEO);
                    break;

                case "moments":
                    perms.add(CommandPermission.MOMENTS);
                    break;

                case "artist-badge":
                    perms.add(CommandPermission.ARTIST);
                    break;

                case "vip":
                    perms.add(CommandPermission.VIP);
                    break;

                case "staff":
                case "admin":
                    perms.add(CommandPermission.TWITCHSTAFF);
                    break;

                case "moderator":
                    perms.add(CommandPermission.MODERATOR);
                    break;

                case "broadcaster":
                    perms.add(CommandPermission.BROADCASTER);
                    perms.add(CommandPermission.MODERATOR);
                    break;

                default:
                    break;
            }
        }
        return perms;
    }

    private static Set<CommandPermission> getPermissionsFromTags(@Nullable CharSequence badgesTag, String userId, Collection<String> botOwnerIds, @NonNull Map<String, String> badges) {
        // Parse badges tag
        if (badgesTag != null) {
            badges.putAll(parseBadges(badgesTag));
        }

        // Check for Permissions
        Set<CommandPermission> permissionSet = getPermissions(badges.entrySet(), Map.Entry::getKey, Map.Entry::getValue);

        if (userId != null && botOwnerIds != null && botOwnerIds.contains(userId))
            permissionSet.add(CommandPermission.OWNER);

        return permissionSet;
    }

    /**
     * Parse Badges from raw list
     *
     * @param raw The raw list of tags.
     * @return A key-value map of the tags.
     */
    public static Map<String, String> parseBadges(CharSequence raw) {
        if (StringUtils.isEmpty(raw)) return Collections.emptyMap();

        // Fix Whitespaces
        String tagValue = EscapeUtils.unescapeTagValue(raw);

        String[] parts = StringUtils.split(tagValue, ',');
        Map<String, String> map = new HashMap<>(parts.length * 4 / 3 + 1);
        for (String tag : parts) {
            int i = tag.indexOf('/');
            String key, value;
            if (i < 0) {
                key = tag;
                value = null;
            } else {
                key = tag.substring(0, i);
                value = tag.substring(i + 1);
            }
            map.put(key, value);
        }

        return Collections.unmodifiableMap(map); // formatting to Read-Only Map
    }

}
