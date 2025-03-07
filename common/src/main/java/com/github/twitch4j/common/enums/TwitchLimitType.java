package com.github.twitch4j.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The types of rate-limits imposed by Twitch.
 */
@RequiredArgsConstructor
public enum TwitchLimitType {

    /**
     * How fast authentication attempts can be issued over IRC.
     */
    CHAT_AUTH_LIMIT("irc-auth-limit"),

    /**
     * How fast channel join attempts can be issued over IRC.
     */
    CHAT_JOIN_LIMIT("irc-join-limit"),

    /**
     * How fast messages can be issued over IRC.
     */
    CHAT_MESSAGE_LIMIT("irc-msg-limit"),

    /**
     * How fast private messages can be issued over IRC.
     */
    CHAT_WHISPER_LIMIT("irc-whisper-limit"),

    /**
     * How fast Check AutoMod Status can be called per channel via Helix.
     *
     * @see <a href="https://discuss.dev.twitch.tv/t/upcoming-changes-to-the-check-automod-status-api-endpoint/38512#rate-limits-2">Twitch Announcement</a>
     */
    HELIX_AUTOMOD_STATUS_LIMIT("helix-automod_status-limit"),

    /**
     * How fast Chat Announcements can be sent in a given channel.
     */
    HELIX_ANNOUNCEMENT_LIMIT("helix-announcement-limit");

    /**
     * The identifier for the related bandwidth slot in a bucket for smoother replacement.
     */
    @Getter
    private final String bandwidthId;

}
