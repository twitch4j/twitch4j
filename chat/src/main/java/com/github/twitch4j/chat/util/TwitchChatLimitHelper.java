package com.github.twitch4j.chat.util;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
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

    /**
     * Users sending commands or messages to channels in which they do not have Moderator or Operator status
     */
    public final Bandwidth USER_MESSAGE_LIMIT = Bandwidth.simple(20, Duration.ofSeconds(30));

    /**
     * Users sending commands or messages to channels in which they have Moderator or Operator status
     */
    public final Bandwidth MOD_MESSAGE_LIMIT = Bandwidth.simple(100, Duration.ofSeconds(30));

    /**
     * Known bots
     */
    public final Bandwidth KNOWN_MESSAGE_LIMIT = Bandwidth.simple(50, Duration.ofSeconds(30));

    /**
     * Verified bots
     */
    public final Bandwidth VERIFIED_MESSAGE_LIMIT = Bandwidth.simple(7500, Duration.ofSeconds(30));

    /**
     * Users (not bots)
     */
    public final List<Bandwidth> USER_WHISPER_LIMIT = Collections.unmodifiableList(
        Arrays.asList(
            Bandwidth.simple(100, Duration.ofSeconds(60)),
            Bandwidth.simple(3, Duration.ofSeconds(1))
        )
    );

    /**
     * Known bots
     */
    public final List<Bandwidth> KNOWN_WHISPER_LIMIT = Collections.unmodifiableList(
        Arrays.asList(
            Bandwidth.simple(200, Duration.ofSeconds(60)),
            Bandwidth.simple(10, Duration.ofSeconds(1))
        )
    );

    /**
     * Verified bots
     */
    public final List<Bandwidth> VERIFIED_WHISPER_LIMIT = Collections.unmodifiableList(
        Arrays.asList(
            Bandwidth.simple(1200, Duration.ofSeconds(60)),
            Bandwidth.simple(20, Duration.ofSeconds(1))
        )
    );

    /**
     * Join rate for users
     */
    public final Bandwidth USER_JOIN_LIMIT = Bandwidth.simple(20, Duration.ofSeconds(10));

    /**
     * Join rate for verified bots
     */
    public final Bandwidth VERIFIED_JOIN_LIMIT = Bandwidth.simple(2000, Duration.ofSeconds(10));

    /**
     * Authentication rate for users
     */
    public final Bandwidth USER_AUTH_LIMIT = Bandwidth.simple(20, Duration.ofSeconds(10));

    /**
     * Authentication rate for verified bots
     */
    public final Bandwidth VERIFIED_AUTH_LIMIT = Bandwidth.simple(200, Duration.ofSeconds(10));

    public Bucket createBucket(Bandwidth limit) {
        return Bucket4j.builder().addLimit(limit).build();
    }

    public Bucket createBucket(Bandwidth... limits) {
        LocalBucketBuilder builder = Bucket4j.builder();
        for (Bandwidth limit : limits) {
            builder.addLimit(limit);
        }
        return builder.build();
    }

    public Bucket createBucket(Iterable<Bandwidth> limits) {
        LocalBucketBuilder builder = Bucket4j.builder();
        for (Bandwidth limit : limits) {
            builder.addLimit(limit);
        }
        return builder.build();
    }

}
