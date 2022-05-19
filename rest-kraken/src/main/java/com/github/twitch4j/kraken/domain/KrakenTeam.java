package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * Model representing a team.
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Data
@Deprecated
public class KrakenTeam {

    @JsonProperty("_id")
    private long id;

    private String name;

    private String displayName;

    private String info;

    private String logo;

    private String background;

    private String banner;

    @JsonProperty("created_at")
    private Instant createdAtInstant;

    @JsonProperty("updated_at")
    private Instant updatedAtInstant;

    private List<KrakenTeamUser> users;

    /**
     * @return the timestamp of when the team was created
     * @deprecated in favor of getCreatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getCreatedAt() {
        return Date.from(createdAtInstant);
    }

    /**
     * @return the timestamp of when the team was last updated
     * @deprecated in favor of getUpdatedAtInstant()
     */
    @JsonIgnore
    @Deprecated
    public Date getUpdatedAt() {
        return Date.from(updatedAtInstant);
    }
}
