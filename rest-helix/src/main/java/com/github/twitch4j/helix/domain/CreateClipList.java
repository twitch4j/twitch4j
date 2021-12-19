package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

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
