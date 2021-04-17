package com.github.twitch4j.helix.domain;

import lombok.*;

import java.util.List;

/**
 * Clip Data
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class CreateClipList {

    @NonNull
    private List<CreateClip> data;

}
