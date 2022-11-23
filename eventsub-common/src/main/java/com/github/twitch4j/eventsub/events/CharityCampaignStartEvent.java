package com.github.twitch4j.eventsub.events;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CharityCampaignStartEvent extends ChannelCharityCampaignMetaEvent {

    /**
     * The UTC timestamp (in RFC3339 format) of when the broadcaster started the campaign.
     */
    private Instant startedAt;

}
