package com.github.twitch4j.common.util;

import com.github.twitch4j.common.enums.CommandPermission;
import com.github.twitch4j.common.events.domain.EventUser;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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

    public static Set<CommandPermission> getPermissionsFromTags(Map<String, Object> tags) {
        return getPermissionsFromTags(tags, new HashMap<>());
    }

    public static Set<CommandPermission> getPermissionsFromTags(@NonNull Map<String, Object> tags, @NonNull Map<String, String> badges) {
        return getPermissionsFromTags(tags, badges, null, null);
    }

    public static Set<CommandPermission> getPermissionsFromTags(@NonNull Map<String, Object> tags, @NonNull Map<String, String> badges, String userId, Collection<String> botOwnerIds) {
        Set<CommandPermission> permissionSet = EnumSet.of(CommandPermission.EVERYONE);

        // Check for Permissions
        if (tags.containsKey("badges")) {
            if (tags.get("badges") instanceof String) {
                // needed for irc
                badges.putAll(parseBadges((String) tags.get("badges")));
            } else {
                List<Map<String, String>> badgeList = (List<Map<String, String>>) tags.get("badges");
                if (badgeList != null) {
                    badgeList.forEach(badge -> badges.put(badge.get("id"), "1"));
                }
            }

            // Broadcaster
            if (badges.containsKey("broadcaster")) {
                permissionSet.add(CommandPermission.BROADCASTER);
                permissionSet.add(CommandPermission.MODERATOR);
            }
            // Twitch Prime
            if (badges.containsKey("premium")) {
                permissionSet.add(CommandPermission.PRIME_TURBO);
            }
            // Moderator
            if (badges.containsKey("moderator")) {
                permissionSet.add(CommandPermission.MODERATOR);
            }
            // Partner
            if (badges.containsKey("partner")) {
                permissionSet.add(CommandPermission.PARTNER);
            }
            // VIP
            if (badges.containsKey("vip")) {
                permissionSet.add(CommandPermission.VIP);
            }
            // Turbo
            if (badges.containsKey("turbo")) {
                permissionSet.add(CommandPermission.PRIME_TURBO);
            }
            // Twitch Staff
            if (badges.containsKey("staff")) {
                permissionSet.add(CommandPermission.TWITCHSTAFF);
            }
            // Subscriber
            if(badges.containsKey("subscriber")) {
                permissionSet.add(CommandPermission.SUBSCRIBER);
            }
            // SubGifter
            if(badges.containsKey("sub-gifter")) {
                permissionSet.add(CommandPermission.SUBGIFTER);
            }
            // Founder
            if(badges.containsKey("founder")) {
                permissionSet.add(CommandPermission.FOUNDER);
                permissionSet.add(CommandPermission.SUBSCRIBER);

                // also contains info about the tier if needed
                /*
                if (badges.get("founder").equals("0")) {
                    // Tier 1 Founder
                } else if (badges.get("founder").equals("1")) {
                    // Tier 2 Founder
                } else if (badges.get("founder").equals("2")) {
                    // Tier 3 Founder
                }
                */
            }
            // Hype Train Conductor
            String hypeBadge = badges.get("hype-train");
            if ("1".equals(hypeBadge)) {
                permissionSet.add(CommandPermission.CURRENT_HYPE_TRAIN_CONDUCTOR);
            } else if ("2".equals(hypeBadge)) {
                permissionSet.add(CommandPermission.FORMER_HYPE_TRAIN_CONDUCTOR);
            }
            // Predictions Participation
            String predictionBadge = badges.get("predictions");
            if (StringUtils.isNotEmpty(predictionBadge)) {
                char first = predictionBadge.charAt(0);
                char last = predictionBadge.charAt(predictionBadge.length() - 1);
                if (first == 'b' || last == '1') {
                    permissionSet.add(CommandPermission.PREDICTIONS_BLUE);
                } else if (first == 'p' || last == '2') {
                    permissionSet.add(CommandPermission.PREDICTIONS_PINK);
                }
            }
        }

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
    public static Map<String, String> parseBadges(String raw) {
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

}
