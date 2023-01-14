package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.common.util.TypeConvert;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class CreateShoutoutData {

    @JsonProperty("broadcasterUserID")
    private String broadcasterId;

    @JsonProperty("targetUserID")
    private String targetId;

    @JsonProperty("targetLogin")
    private String targetLogin;

    @JsonProperty("targetUserDisplayName")
    private String targetDisplayName;

    @JsonProperty("targetUserProfileImageURL")
    private String targetProfileUrlTemplate; // contains %s for the image dimensions

    @JsonProperty("targetUserPrimaryColorHex")
    private String targetUserColorHex; // can be empty string if not set

    @JsonProperty("sourceUserID")
    private String moderatorId;

    @JsonProperty("sourceLogin")
    private String moderatorLogin;

    @JsonProperty("shoutoutID")
    private String shoutoutId;

    @JsonIgnore
    private ShoutoutTargetCallToAction targetStreamsInfo;

    @JsonProperty("targetUserCTAInfo")
    private void unpackCallToAction(String ctaInfo) {
        if (ctaInfo == null || ctaInfo.isEmpty()) return;
        this.targetStreamsInfo = TypeConvert.jsonToObject(ctaInfo, ShoutoutTargetCallToAction.class);
    }

}
