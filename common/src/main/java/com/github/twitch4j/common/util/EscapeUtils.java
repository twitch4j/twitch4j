package com.github.twitch4j.common.util;

import org.apache.commons.lang3.StringUtils;

public class EscapeUtils {

    /**
     * Escapes a value for use in an IRCv3 message tag.
     *
     * @param value the unescaped message tag value
     * @return the escaped tag value
     * @see <a href="https://ircv3.net/specs/extensions/message-tags.html">Offical spec</a>
     */
    public static String escapeTagValue(Object value) {
        if (value == null) return "";
        char[] unescaped = value.toString().toCharArray();
        StringBuilder sb = new StringBuilder(unescaped.length);
        for (char c : unescaped) {
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
     * @see <a href="https://ircv3.net/specs/extensions/message-tags.html">Offical spec</a>
     */
    public static String unescapeTagValue(String value) {
        return StringUtils.replaceEach(
            value,
            new String[] {
                "\\:",
                "\\s",
                "\\\\",
                "\\r",
                "\\n"
            },
            new String[] {
                ";",
                " ",
                "\\",
                "\r",
                "\n"
            }
        );
    }

}
