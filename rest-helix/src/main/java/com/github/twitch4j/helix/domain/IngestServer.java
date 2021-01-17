package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngestServer {

    /**
     * Sequential identifier of ingest server.
     */
    @JsonProperty("_id")
    private Integer id;

    /**
     * Reserved for internal use.
     */
    private Float availability;

    /**
     * Reserved for internal use.
     */
    @JsonProperty("default")
    @Accessors(fluent = true)
    private Boolean isDefault;

    /**
     * Descriptive name of ingest server.
     */
    private String name;

    /**
     * RTMP URL template for ingest server
     */
    private String urlTemplate;

    /**
     * Reserved for internal use.
     */
    private Integer priority;

}
