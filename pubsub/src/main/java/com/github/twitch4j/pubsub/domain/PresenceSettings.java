package com.github.twitch4j.pubsub.domain;

import lombok.Data;

/**
 * @deprecated Friends are being removed by <a href="https://help.twitch.tv/s/article/how-to-use-the-friends-feature">Twitch</a> on 2022-05-25
 */
@Data
@Deprecated
public class PresenceSettings {
    private Boolean shareActivity;
    private String availabilityOverride;
    private Boolean isInvisible;
    private String share;
}
