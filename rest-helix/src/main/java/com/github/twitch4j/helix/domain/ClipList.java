package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Clip List
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ClipList {

    private List<Clip> data;

    private HelixPagination pagination;

}
