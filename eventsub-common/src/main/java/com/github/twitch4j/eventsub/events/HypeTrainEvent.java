package com.github.twitch4j.eventsub.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.twitch4j.eventsub.domain.Contribution;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class HypeTrainEvent extends EventSubChannelEvent {

    /**
     * The Hype Train ID.
     */
    private String id;

    /**
     * Total points contributed to hype train.
     */
    private Integer total;

    /**
     * The contributors with the most points contributed.
     */
    private List<Contribution> topContributions;

    /**
     * Current level of hype train event.
     */
    private Integer level;

    /**
     * The timestamp at which the hype train started.
     */
    private Instant startedAt;

    /**
     * Whether the hype train rewards contributors with a temporary golden Kappa emote.
     */
    @Accessors(fluent = true)
    @JsonProperty("is_golden_kappa_train")
    private boolean isGoldenKappaTrain;

}
