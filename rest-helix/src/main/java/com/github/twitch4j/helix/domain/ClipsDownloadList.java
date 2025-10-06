package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class ClipsDownloadList {

    /**
     * The clips and their download URLs.
     */
    private List<ClipsDownload> data;

}
