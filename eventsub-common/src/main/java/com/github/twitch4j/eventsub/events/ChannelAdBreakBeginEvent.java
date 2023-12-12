package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ChannelAdBreakBeginEvent extends EventSubChannelEvent {

    /**
     * Length in seconds of the mid-roll ad break requested.
     */
    @JsonAlias({"duration", "duration_seconds"}) // https://github.com/twitchdev/issues/issues/850
    private Integer lengthSeconds;

    /**
     * The UTC timestamp of when the ad break began, in RFC3339 format.
     * <p>
     * Note that there is potential delay between this event,
     * when the streamer requested the ad break, and when the viewers will see ads.
     */
    @JsonAlias("timestamp") // https://github.com/twitchdev/issues/issues/850
    private Instant startedAt;

    /**
     * Indicates if the ad was automatically scheduled via Ads Manager.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_automatic")
    private Boolean isAutomatic;

    /**
     * The ID of the user that requested the ad.
     * For automatic ads, this will be the ID of the broadcaster.
     */
    private String requesterUserId;

    /**
     * The login of the user that requested the ad.
     */
    private String requesterUserLogin;

    /**
     * The display name of the user that requested the ad.
     */
    private String requesterUserName;

}
