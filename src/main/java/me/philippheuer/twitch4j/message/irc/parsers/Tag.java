package me.philippheuer.twitch4j.message.irc.parsers;

import java.io.Serializable;
import java.util.*;

@SuppressWarnings("unchecked")
public class Tag extends HashMap implements Map, Serializable {

    private Tag() {
        super();
    }

    public static Map parse(String raw) {
        Tag tags = new Tag();

        if (raw == null) return null;

        for (String tag: raw.split(";")) {
            String[] val = tag.split("=");
            final String key = val[0];
            String value = (val.length > 1) ? val[1] : null;
            tags.put(key, parseValue(key, value));
        }

        return Collections.unmodifiableMap(tags); // formatting to Read-Only Map
    }


    private static <T extends Object> T parseValue(String key, String value) {
        if (value == null) return null;
        switch (key.toLowerCase()) {
            case "badges":
                Map<String, String> badges = new HashMap<String, String>();
                for (String badge : value.split(",")) {
                    final String name = badge.split("/")[0];
                    final String version = badge.split("/")[1];
                    badges.put(name, version);
                }
                return (T) badges;
            case "emotes":
                Map<String, List<List<String>>> emotes = new HashMap<>();
                for (String emote : value.split("/")) {
                    final String emoteId = emote.split(":")[0];
                    final List<List<String>> emoteLocations = new ArrayList<List<String>>();
                    for (String emoteLocate : emote.split(":")[1].split(",")){
                        emoteLocations.add(Arrays.asList(emoteLocate.split("-")));
                    }
                    emotes.put(emoteId, emoteLocations);
                }
                return (T) emotes;
            case "emote-sets":
                return (T) Arrays.asList(value.split(","));
            case "subscriber":
            case "mod":
            case "turbo":
                return (T) Boolean.valueOf(value.equals("1"));
            default:
                return (T) value.replace("\\s", " ");
        }
    }
}
