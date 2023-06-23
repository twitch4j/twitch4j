package com.github.twitch4j.common.util;

import org.apache.commons.lang3.StringUtils;

public class EscapeUtils {

    /**
     * Escapes a value for use in an IRCv3 message tag.
     *
     * @param value the unescaped message tag value
     * @return the escaped tag value
     * @see <a href="https://ircv3.net/specs/extensions/message-tags.html">Official spec</a>
     */
    public static String escapeTagValue(Object value) {
        final String unescapedString;
        if (value == null || (unescapedString = value.toString()) == null) return "";

        final char[] unescaped = unescapedString.toCharArray();
        final int n = unescaped.length;

        // Determine the index of the first replacement needed in the string
        int firstReplacement = -1;
        for (int i = 0; i < n; i++) {
            char c = unescaped[i];
            if (c == ';' || c == ' ' || c == '\\' || c == '\r' || c == '\n') {
                firstReplacement = i;
                break;
            }
        }

        // When no replacements are needed, skip allocating the StringBuilder and copying over the chars
        if (firstReplacement < 0) return unescapedString;

        // Otherwise: replacement(s) are needed
        final StringBuilder sb = new StringBuilder(n + 1); // Set capacity to length of string plus one replacement
        sb.append(unescaped, 0, firstReplacement); // Copy over the region of the string that requires no replacements
        for (int i = firstReplacement; i < n; i++) { // Perform replacements on the rest of the string as needed
            char c = unescaped[i];
            switch (c) {
                case ';':
                    sb.append("\\:");
                    break;
                case ' ':
                    sb.append("\\s");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Unescapes a value used in a IRCv3 message tag.
     *
     * @param value the escaped message tag value
     * @return the unescaped value
     * @see <a href="https://ircv3.net/specs/extensions/message-tags.html">Official spec</a>
     */
    public static String unescapeTagValue(CharSequence value) {
        if (value == null)
            return null;

        final int start = StringUtils.indexOf(value, '\\');
        if (start < 0)
            return value.toString();

        final int len = value.length();
        final StringBuilder sb = new StringBuilder(len - 1);
        sb.append(value, 0, start);

        boolean escapeNext = true;
        for (int i = start + 1; i < len; i++) {
            char c = value.charAt(i);
            if (escapeNext) {
                switch (c) {
                    case ':':
                        sb.append(';');
                        break;
                    case 's':
                        sb.append(' ');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    default:
                        sb.append(c);
                        break;
                }
                escapeNext = false;
            } else {
                if (c == '\\') {
                    escapeNext = true;
                } else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

}
