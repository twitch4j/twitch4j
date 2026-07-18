package com.github.twitch4j.chat.util;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Value
public class GifChatTag {

    int startPosition;
    int endPosition;
    String gifId;
    String gifUrl;

    public static GifChatTag of(String tagValue) {
        if (tagValue == null || tagValue.isEmpty()) return null;

        String[] parts = StringUtils.split(tagValue, '|');
        if (parts.length < 3) return null;

        int positionDelim = parts[0].indexOf('-');
        if (positionDelim < 0) return null;

        int startPosition;
        int endPosition;
        try {
            startPosition = Integer.parseInt(parts[0].substring(0, positionDelim));
            endPosition = Integer.parseInt(parts[0].substring(positionDelim + 1));
        } catch (NumberFormatException ignored) {
            return null;
        }
        String gifId = parts[1];
        String gifUrl = parts[2];
        return new GifChatTag(startPosition, endPosition, gifId, gifUrl);
    }

    public static List<GifChatTag> parseList(String tagValue) {
        if (tagValue == null || tagValue.isEmpty()) return null;

        String[] parts = StringUtils.split(tagValue, ',');
        List<GifChatTag> tags = new ArrayList<>(parts.length);

        for (String part : parts) {
            GifChatTag tag = GifChatTag.of(part);
            if (tag != null) tags.add(tag);
        }

        return tags;
    }

}
