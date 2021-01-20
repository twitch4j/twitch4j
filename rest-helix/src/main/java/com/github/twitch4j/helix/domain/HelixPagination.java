package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Pagination
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class HelixPagination {

    private String cursor;

}
