package com.github.twitch4j.chat.util;

import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.nio.CharBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class MessageParser {

    @Nullable
    @VisibleForTesting
    public IRCMessageEvent parse(@NotNull String rawMessage) {
        return parse(rawMessage, Collections.emptyMap(), Collections.emptyMap(), null);
    }

    @Nullable
    @ApiStatus.Internal
    public IRCMessageEvent parse(@NotNull String raw, @NotNull Map<String, String> channelIdToChannelName, @NotNull Map<String, String> channelNameToChannelId, @Nullable Collection<String> botOwnerIds) {
        final int len = raw.length();
        int i = 0;

        // Tags
        final Map<String, CharSequence> tags;
        if (raw.startsWith("@")) {
            tags = new HashMap<>(32);
            i = parseTags(raw, tags);
        } else {
            tags = Collections.emptyMap();
        }

        // Client
        if (raw.charAt(i) == ':') i++;
        int exclamation = -1;
        int space = -1;
        for (int j = i; j < len; j++) {
            final char c = raw.charAt(j);
            if (c == '!') {
                if (exclamation < 0)
                    exclamation = j;
            } else if (c == ' ') {
                space = j;
                break;
            }
        }
        if (space < 0 || space + 1 >= len) return null;
        final int clientNameEnd = exclamation > 0 ? exclamation : space;
        final CharSequence clientName = CharBuffer.wrap(raw, i, clientNameEnd);
        i = space + 1;

        // Command
        int commandEnd = raw.indexOf(' ', i);
        if (commandEnd < 0) {
            commandEnd = len;
        }
        final String commandType = raw.substring(i, commandEnd);
        i = commandEnd + 1;

        if (i >= len) {
            return new IRCMessageEvent(raw, tags, clientName, commandType, null, null, null, channelIdToChannelName, channelNameToChannelId, botOwnerIds);
        }

        // Channel
        int messageStart = raw.indexOf(" :", i);
        if (messageStart < 0) {
            messageStart = len;
        }
        final CharSequence channel = CharBuffer.wrap(raw, i, messageStart);
        final int chanDelim = StringUtils.indexOf(channel, " = "); // handle 353 NAMES
        final CharSequence channelPart = chanDelim < 0 ? channel : channel.subSequence(chanDelim + " = ".length(), channel.length());
        final String channelName = (
            channelPart.length() > 0 && channelPart.charAt(0) == '#'
                ? channelPart.subSequence(1, channelPart.length())
                : channelPart
        ).toString();

        // Message
        messageStart++;
        if (messageStart >= len) {
            return new IRCMessageEvent(raw, tags, clientName, commandType, channelName, null, null, channelIdToChannelName, channelNameToChannelId, botOwnerIds);
        }
        final CharSequence payload = CharBuffer.wrap(raw, messageStart, len);
        final String message = raw.substring(messageStart + 1);
        return new IRCMessageEvent(raw, tags, clientName, commandType, channelName, payload, message, channelIdToChannelName, channelNameToChannelId, botOwnerIds);
    }

    @VisibleForTesting
    public int parseTags(String input, Map<String, CharSequence> output) {
        final int len = input.length();
        int i = 0;
        for (int j = i + 1; j < len; j++) {
            final char c = input.charAt(j);
            final boolean delim = c == ';';
            if (delim || c == ' ') {
                final CharSequence tag = CharBuffer.wrap(input, i + 1, j);
                parseTag(output, tag);
                i = j;
                if (!delim) break;
            }
        }
        return ++i;
    }

    private void parseTag(Map<String, CharSequence> tags, CharSequence tag) {
        final int i = StringUtils.indexOf(tag, '=');
        final CharSequence key, value;
        if (i < 0) {
            key = tag;
            value = null;
        } else {
            key = tag.subSequence(0, i);
            value = tag.subSequence(i + 1, tag.length());
        }
        tags.put(key.toString(), value);
    }

}
