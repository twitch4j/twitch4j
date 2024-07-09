package com.github.twitch4j.helix.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

/**
 * A generic container for helix responses that contain a single value.
 *
 * @param <T> the type of value contained in the wrapper
 */
@Data
@Setter(AccessLevel.PRIVATE)
public class ValueWrapper<T> {

    /**
     * The data returned from the endpoint.
     */
    private List<T> data;

    /**
     * @return the single value contained in the response, or null.
     */
    public T get() {
        return data != null && !data.isEmpty() ? data.get(0) : null;
    }

}
