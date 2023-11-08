package com.github.twitch4j.eventsub.domain.chat;

import com.github.twitch4j.common.enums.CommandPermission;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

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

    /**
     * @return {@link #getSetId()} as {@link CommandPermission}, or null if there is no corresponding enum value.
     */
    @Nullable
    public CommandPermission asEnum() {
        switch (setId) {
            case "premium":
            case "turbo":
                return CommandPermission.PRIME_TURBO;

            case "partner":
            case "ambassador":
                return CommandPermission.PARTNER;

            case "subscriber":
                return CommandPermission.SUBSCRIBER;

            case "founder":
                return CommandPermission.FOUNDER; // unconfirmed

            case "sub-gifter":
            case "sub-gift-leader":
                return CommandPermission.SUBGIFTER;

            case "bits":
            case "bits-leader":
                return CommandPermission.BITS_CHEERER;

            case "hype-train":
                if ("2".equals(id))
                    return CommandPermission.FORMER_HYPE_TRAIN_CONDUCTOR;
                return CommandPermission.CURRENT_HYPE_TRAIN_CONDUCTOR;

            case "no_audio":
                return CommandPermission.NO_AUDIO;

            case "no_video":
                return CommandPermission.NO_VIDEO;

            case "moments":
                return CommandPermission.MOMENTS;

            case "artist":
            case "artist-badge":
                return CommandPermission.ARTIST; // unconfirmed

            case "vip":
                return CommandPermission.VIP;

            case "staff":
            case "admin":
                return CommandPermission.TWITCHSTAFF; // unconfirmed

            case "moderator":
                return CommandPermission.MODERATOR;

            case "broadcaster":
                return CommandPermission.BROADCASTER;
        }
        return null;
    }
}
