package com.github.twitch4j.eventsub.domain.chat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Badge {

    /**
     * An ID that identifies this set of chat badges.
     * <p>
     * For example: "bits", "subscriber", "vip", "moderator", "broadcaster", "partner", "ambassador", "turbo",
     * "hype-train", "no_audio", "no_video", "sub-gifter", "sub-gift-leader", "premium" (i.e., twitch prime)
     */
    private String setId;

    /**
     * An ID that identifies this version of the badge.
     * The ID can be any value.
     * <p>
     * For example, for Bits, the ID is the Bits tier level,
     * but for World of Warcraft, it could be Alliance or Horde.
     */
    private String id;

    /**
     * Contains metadata related to the chat badges in the badges tag.
     * <p>
     * Currently, this tag contains metadata only for subscriber badges,
     * to indicate the number of months the user has been a subscriber.
     */
    private String info;

}
