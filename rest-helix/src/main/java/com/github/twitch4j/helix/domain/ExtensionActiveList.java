package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Extension Active List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ExtensionActiveList {

    @JsonProperty("data")
    private ActiveExtensions data;

    @Deprecated
    private HelixPagination pagination;

    @Data
    @Setter(AccessLevel.PRIVATE)
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActiveExtensions {

        /**
         * Contains data for panel Extensions.
         */
        @JsonProperty("panel")
        private Map<String, ActiveExtension> activePanels;

        /**
         * Contains data for video-overlay Extensions.
         */
        @JsonProperty("overlay")
        private Map<String, ActiveExtension> activeOverlays;

        /**
         * Contains data for video-component Extensions.
         */
        @JsonProperty("component")
        private Map<String, ActiveExtension> activeComponents;

        @JsonIgnore
        @Deprecated
        public Map<String, Extension> getPanels() {
            return computeFallback(activePanels);
        }

        @JsonIgnore
        @Deprecated
        public Map<String, Extension> getOverlays() {
            return computeFallback(activeOverlays);
        }

        @JsonIgnore
        @Deprecated
        public Map<String, Extension> getComponents() {
            return computeFallback(activeComponents);
        }

        private static Map<String, Extension> computeFallback(Map<String, ActiveExtension> map) {
            return map.keySet().stream().collect(Collectors.toMap(k -> k, k -> map.get(k).asExtension()));
        }
    }

}
