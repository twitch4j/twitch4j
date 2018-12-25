package com.github.twitch4j.common.util;

import com.github.twitch4j.common.enums.CommandPermission;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TwitchUtils {

    public static Set<CommandPermission> getPermissionsFromTags(Map<String, Object> tags) {
        Set<CommandPermission> permissionSet = new HashSet<>();

        // Check for Permissions
        if (tags.containsKey("badges")) {
            final Map<String, Object> badges = new HashMap<>();

            if (tags.get("badges") instanceof String) {
                // needed for irc
                badges.putAll(parseBadges((String) tags.get("badges")));
            } else {
                List<Map<String, String>> badgeList = (List<Map<String, String>>) tags.get("badges");
                if (badgeList != null) {
                    badgeList.forEach(badge -> badges.put(badge.get("id"), "1"));
                }
            }

            // - Broadcaster
            if (badges.containsKey("broadcaster")) {
                permissionSet.add(CommandPermission.BROADCASTER);
                permissionSet.add(CommandPermission.MODERATOR);
            }
            // Twitch Prime
            if (badges.containsKey("premium")) {
                permissionSet.add(CommandPermission.PRIME_TURBO);
            }
            // Partner
            if (badges.containsKey("partner")) {
                permissionSet.add(CommandPermission.PARTNER);
            }
            // VIP
            if (badges.containsKey("vip")) {
                permissionSet.add(CommandPermission.VIP);
            }
            // Twitch Staff
            if (badges.containsKey("staff")) {
                permissionSet.add(CommandPermission.TWITCHSTAFF);
            }
        }
        // Moderator
        if (tags.containsKey("mod") && tags.get("mod").equals("1")) {
            permissionSet.add(CommandPermission.MODERATOR);
        }
        // Twitch Turbo
        if (tags.containsKey("turbo") && tags.get("turbo").equals("1")) {
            permissionSet.add(CommandPermission.PRIME_TURBO);
        }
        // Subscriber
        if (tags.containsKey("subscriber") && tags.get("subscriber").equals("1")) {
            permissionSet.add(CommandPermission.SUBSCRIBER);
        }
        // Sub Gifter
        if (tags.containsKey("sub-gifter") && tags.get("sub-gifter").equals("1")) {
            permissionSet.add(CommandPermission.SUBGIFTER);
        }
        // Everyone
        permissionSet.add(CommandPermission.EVERYONE);

        return permissionSet;
    }

    /**
     * Parse Badges from raw list
     *
     * @param raw The raw list of tags.
     * @return A key-value map of the tags.
     */
    private static Map<String, Object> parseBadges(String raw) {
        Map<String, Object> map = new HashMap<>();
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
