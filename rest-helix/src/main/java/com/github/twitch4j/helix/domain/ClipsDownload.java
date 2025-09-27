package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ClipsDownload {

    /**
     * An ID that uniquely identifies the clip.
     */
    @NotNull
    private String clipId;

    /**
     * The landscape URL to download the clip.
     * This field is null if the URL is not available.
     */
    @Nullable
    private String landscapeDownloadUrl;

    /**
     * The portrait URL to download the clip.
     * This field is null if the URL is not available.
     */
    @Nullable
    private String portraitDownloadUrl;

}
