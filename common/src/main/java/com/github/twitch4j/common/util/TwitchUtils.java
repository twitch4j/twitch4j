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
