package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
public class SharedChatParticipant {
    private Status status;
    private String participantId;
    private String channelId;
    private String channelLogin;
    private String channelDisplayName;
    private @JsonProperty("channel_profile_image_url") String profileImageUrlTemplate;
    private @Nullable String channelPrimaryColorHex;

    public String getProfileImageUrl() {
        return profileImageUrlTemplate.replace("%s", "70x70");
    }

    public enum Status {
        ACTIVE,
        LEFT
    }
}
