package me.philippheuer.twitch4j.exceptions;

import lombok.Getter;
import me.philippheuer.twitch4j.models.IRestError;

@Getter
public class RestException extends RuntimeException {

    private final IRestError error;

    public RestException(IRestError error) {
        super(error.toString());
        this.error = error;
    }
}
