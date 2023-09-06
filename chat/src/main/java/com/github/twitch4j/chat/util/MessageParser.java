package com.github.twitch4j.chat.util;

import com.github.twitch4j.chat.events.channel.IRCMessageEvent;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@UtilityClass
public class MessageParser {

    @Nullable
    @VisibleForTesting
    public IRCMessageEvent parse(@NotNull String rawMessage) {
        return parse(rawMessage, new ConcurrentHashMap<>(0), new ConcurrentHashMap<>(0), null);
    }

    @Nullable
    @ApiStatus.Internal
    public IRCMessageEvent parse(@NotNull String raw, @NotNull Map<String, String> channelIdToChannelName, @NotNull Map<String, String> channelNameToChannelId, @Nullable Collection<String> botOwnerIds) {
        final int len = raw.length();
        if (len == 0) return null;
        final char[] chars = raw.toCharArray();
        int i = 0;

        // Tags
        final Map<String, CharSequence> tags;
        if (chars[0] == '@') {
            tags = new HashMap<>(32);
            i = parseTags(chars, tags);
        } else {
            tags = Collections.emptyMap();
        }

        // Client
        if (chars[i] == ':') i++;
        int exclamation = -1;
        int space = -1;
        for (int j = i; j < len; j++) {
            final char c = chars[j];
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
        final CharSequence clientName = CharBuffer.wrap(chars, i, clientNameEnd - i);
        i = space + 1;

        // Command
        int commandEnd = ArrayUtils.indexOf(chars, ' ', i);
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
        final CharSequence channel = CharBuffer.wrap(chars, i, messageStart - i);
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
        final CharSequence payload = CharBuffer.wrap(chars, messageStart, len - messageStart);
        final String message = raw.substring(messageStart + 1);
        return new IRCMessageEvent(raw, tags, clientName, commandType, channelName, payload, message, channelIdToChannelName, channelNameToChannelId, botOwnerIds);
    }

    @VisibleForTesting
    public int parseTags(char[] inputChars, Map<String, CharSequence> output) {
        final int len = inputChars.length;
        int i = 0;
        int delim = -1;
        for (int j = i + 1; j < len; j++) {
            final char c = inputChars[j];
            final boolean boundary = c == ';';
            if (boundary || c == ' ') {
                final int tagStart = i + 1;
                final CharSequence tag = CharBuffer.wrap(inputChars, tagStart, j - tagStart);
                parseTag(output, tag, delim - tagStart);
                i = j;
                delim = -1;
                if (!boundary) break;
            } else if (c == '=' && delim < 0) {
                delim = j;
            }
        }
        return ++i;
    }

    private void parseTag(Map<String, CharSequence> tags, CharSequence tag, int delim) {
        final CharSequence key, value;
        if (delim < 0) {
            key = tag;
            value = null;
        } else {
            key = tag.subSequence(0, delim);
            value = tag.subSequence(delim + 1, tag.length());
        }
        tags.put(key.toString(), value);
    }

    @ApiStatus.Internal
    public void consumeLines(@NotNull String source, @NotNull Consumer<String> consumer) {
        int len = source.length();
        int start = 0;
        int i;

        while ((i = source.indexOf('\n', start)) >= 0) {
            boolean carriage = i > 0 && source.charAt(i - 1) == '\r';
            consumer.accept(source.substring(start, carriage ? i - 1 : i));
            start = i + 1;
        }

        if (start < len) {
            consumer.accept(source.substring(start));
        }
    }
}
