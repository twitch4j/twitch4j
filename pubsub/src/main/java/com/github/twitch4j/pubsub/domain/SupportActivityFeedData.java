package com.github.twitch4j.pubsub.domain;

import com.github.twitch4j.common.annotation.Unofficial;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.OptionalInt;

@Unofficial
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class SupportActivityFeedData {
    private String channelId;
    private String eventId;
    private String userId;
    private String userLogin;
    private String userDisplayName;
    private String userProfileImageUrl;
    private String action;
    private String source;
    private Integer quantity;

    @Unofficial
    public boolean isBitsCheer() {
        return "BITS".equals(source) && "CHEER".equals(action);
    }

    @Unofficial
    public boolean isSub() {
        return "SUBS".equals(source);
    }

    @Unofficial
    public boolean isGiftSub() {
        return action != null && action.endsWith("GIFTED_SUB");
    }

    @Unofficial
    public OptionalInt getSubTier() {
        if (action != null) {
            int i = action.indexOf("TIER_");
            if (i >= 0) {
                i += "TIER_".length();
                int j = action.indexOf('_', i);
                if (j > i) {
                    try {
                        return OptionalInt.of(Integer.parseInt(action.substring(i, j)));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return OptionalInt.empty();
    }
}
