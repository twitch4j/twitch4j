package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.twitch4j.common.util.AlternativeInstantDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Team {

    /**
     * Users in the specified Team.
     */
    private List<TeamUser> users;

    /**
     * URL of the Team background image.
     */
    @Nullable
    private String backgroundImageUrl;

    /**
     * URL for the Team banner.
     */
    @Nullable
    private String banner;

    /**
     * Date and time the Team was created.
     */
    @JsonDeserialize(using = AlternativeInstantDeserializer.class)
    private Instant createdAt;

    /**
     * Date and time the Team was last updated.
     */
    @JsonDeserialize(using = AlternativeInstantDeserializer.class)
    private Instant updatedAt;

    /**
     * Team description.
     */
    private String info;

    /**
     * Image URL for the Team logo.
     */
    private String thumbnailUrl;

    /**
     * Team name.
     */
    private String teamName;

    /**
     * Team display name.
     */
    private String teamDisplayName;

    /**
     * Team ID.
     */
    private String id;

}
