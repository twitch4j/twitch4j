package com.github.twitch4j.chat.util;

import com.github.twitch4j.common.enums.TwitchLimitType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucketBuilder;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for retrieving standard rate limits for {@link com.github.twitch4j.chat.TwitchChat}
 *
 * @see <a href="https://dev.twitch.tv/docs/irc/guide#command--message-limits">Official documentation</a>
 */
@UtilityClass
public class TwitchChatLimitHelper {

    public final String MESSAGE_BANDWIDTH_ID = TwitchLimitType.CHAT_MESSAGE_LIMIT.getBandwidthId();
    public final String JOIN_BANDWIDTH_ID = TwitchLimitType.CHAT_JOIN_LIMIT.getBandwidthId();
    public final String AUTH_BANDWIDTH_ID = TwitchLimitType.CHAT_AUTH_LIMIT.getBandwidthId();
    public final String WHISPER_MINUTE_BANDWIDTH_ID = TwitchLimitType.CHAT_WHISPER_LIMIT.getBandwidthId() + "-minute";
    public final String WHISPER_SECOND_BANDWIDTH_ID = TwitchLimitType.CHAT_WHISPER_LIMIT.getBandwidthId() + "-second";

    /**
     * Users sending commands or messages to channels in which they do not have Moderator or Operator status
     */
    public final Bandwidth USER_MESSAGE_LIMIT = Bandwidth.simple(20, Duration.ofSeconds(30)).withId(MESSAGE_BANDWIDTH_ID);

    /**
     * Users sending commands or messages to channels in which they have Moderator or Operator status
     */
    public final Bandwidth MOD_MESSAGE_LIMIT = Bandwidth.simple(100, Duration.ofSeconds(30)).withId(MESSAGE_BANDWIDTH_ID);

    /**
     * Known bots
     */
    public final Bandwidth KNOWN_MESSAGE_LIMIT = Bandwidth.simple(50, Duration.ofSeconds(30)).withId(MESSAGE_BANDWIDTH_ID);

    /**
     * Verified bots
     */
    public final Bandwidth VERIFIED_MESSAGE_LIMIT = Bandwidth.simple(7500, Duration.ofSeconds(30)).withId(MESSAGE_BANDWIDTH_ID);

    /**
     * Users (not bots)
     * <p>
     * Note: this does <i>not</i> implement the target user count restriction.
     */
    public final List<Bandwidth> USER_WHISPER_LIMIT = Collections.unmodifiableList(
        Arrays.asList(
            Bandwidth.simple(100, Duration.ofSeconds(60)).withId(WHISPER_MINUTE_BANDWIDTH_ID),
            Bandwidth.simple(3, Duration.ofSeconds(1)).withId(WHISPER_SECOND_BANDWIDTH_ID)
        )
    );

    /**
     * Known bots
     * <p>
     * Note: this does <i>not</i> implement the target user count restriction.
     */
    public final List<Bandwidth> KNOWN_WHISPER_LIMIT = Collections.unmodifiableList(
        Arrays.asList(
            Bandwidth.simple(200, Duration.ofSeconds(60)).withId(WHISPER_MINUTE_BANDWIDTH_ID),
            Bandwidth.simple(10, Duration.ofSeconds(1)).withId(WHISPER_SECOND_BANDWIDTH_ID)
        )
    );

    /**
     * Verified bots
     * <p>
     * Note: this does <i>not</i> implement the target user count restriction.
     * <p>
     * Note: this only applies to legacy verified bots (pre July 2021)
     */
    public final List<Bandwidth> VERIFIED_WHISPER_LIMIT = Collections.unmodifiableList(
        Arrays.asList(
            Bandwidth.simple(1200, Duration.ofSeconds(60)).withId(WHISPER_MINUTE_BANDWIDTH_ID),
            Bandwidth.simple(20, Duration.ofSeconds(1)).withId(WHISPER_SECOND_BANDWIDTH_ID)
        )
    );

    /**
     * Join rate for users
     */
    public final Bandwidth USER_JOIN_LIMIT = Bandwidth.simple(20, Duration.ofSeconds(10)).withId(JOIN_BANDWIDTH_ID);

    /**
     * Join rate for verified bots
     */
    public final Bandwidth VERIFIED_JOIN_LIMIT = Bandwidth.simple(2000, Duration.ofSeconds(10)).withId(JOIN_BANDWIDTH_ID);

    /**
     * Authentication rate for users
     */
    public final Bandwidth USER_AUTH_LIMIT = Bandwidth.simple(20, Duration.ofSeconds(10)).withId(AUTH_BANDWIDTH_ID);

    /**
     * Authentication rate for verified bots
     */
    public final Bandwidth VERIFIED_AUTH_LIMIT = Bandwidth.simple(200, Duration.ofSeconds(10)).withId(AUTH_BANDWIDTH_ID);

    public Bucket createBucket(Bandwidth limit) {
        return Bucket.builder().addLimit(limit).build();
    }

    public Bucket createBucket(Bandwidth... limits) {
        LocalBucketBuilder builder = Bucket.builder();
        for (Bandwidth limit : limits) {
            builder.addLimit(limit);
        }
        return builder.build();
    }

    public Bucket createBucket(Iterable<Bandwidth> limits) {
        LocalBucketBuilder builder = Bucket.builder();
        for (Bandwidth limit : limits) {
            builder.addLimit(limit);
        }
        return builder.build();
    }

}
