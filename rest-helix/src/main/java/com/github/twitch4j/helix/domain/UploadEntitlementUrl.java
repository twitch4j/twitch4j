package com.github.twitch4j.helix.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UploadEntitlementUrl {

    /**
     * The URL where you will upload the manifest file
     * This is the URL of a pre-signed S3 bucket
     * Lease time: 15 minutes
     */
    @JsonIgnore
    private String url;

    @JsonProperty("url")
    private void unpackUrl(String url) {
        this.url = url.replace("\\u0026", "&");
    }

}
