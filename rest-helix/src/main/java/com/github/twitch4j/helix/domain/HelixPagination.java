package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

/**
 * Pagination
 */
@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class HelixPagination {

    /**
     * The cursor used to get the next page of results. Use the cursor to set the request’s after query parameter.
     */
    @Nullable
    private String cursor;

}
