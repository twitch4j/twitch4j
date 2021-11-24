package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class BlockedTerm {

    /**
     * An ID that uniquely identifies this blocked term.
     */
    private String id;

    /**
     * The broadcaster that owns the list of blocked terms.
     */
    private String broadcasterId;

    /**
     * The moderator that blocked the word or phrase from being used in the broadcaster’s chat room.
     */
    private String moderatorId;

    /**
     * The blocked word or phrase.
     */
    private String text;

    /**
     * The UTC date and time (in RFC3339 format) of when the term was blocked.
     */
    private Instant createdAt;

    /**
     * The UTC date and time (in RFC3339 format) of when the term was updated.
     * <p>
     * When the term is added, this timestamp is the same as created_at.
     * The timestamp changes as AutoMod continues to deny the term.
     */
    private Instant updatedAt;

    /**
     * The UTC date and time (in RFC3339 format) of when the blocked term is set to expire.
     * After the block expires, user’s will be able to use the term in the broadcaster’s chat room.
     * <p>
     * This field is null if the term was added manually or was permanently blocked by AutoMod.
     */
    @Nullable
    private Instant expiresAt;

}
