package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
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
