package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Model representing the response for Create Stream Marker.
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StreamMarker {
    
    private String createdAt, description, id;
    private Long positionSeconds;
    
    @JsonProperty("data")
    private void unpack(List<Map<String, String>> data) {
        if(!data.isEmpty()) {
            Map<String, String> marker = data.get(0);
            createdAt = marker.get("created_at");
            description = marker.get("description");
            id = marker.get("id");
            positionSeconds = Long.parseLong(marker.get("position_seconds"));
        }
    }
    
}
