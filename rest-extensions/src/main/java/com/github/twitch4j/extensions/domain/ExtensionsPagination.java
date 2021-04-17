package com.github.twitch4j.extensions.domain;

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
public class ExtensionsPagination {

    /**
     * Cursor value returned from a prior call to this endpoint. The cursor specifies the starting point of the results.
     */
    private String cursor;

}
