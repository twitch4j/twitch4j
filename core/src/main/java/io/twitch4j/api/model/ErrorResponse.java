package io.twitch4j.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.impl.api.model.ErrorResponseBuilder;

@JsonDeserialize(builder = ErrorResponseBuilder.class)
public abstract class ErrorResponse {
    public abstract String error();

    public abstract String message();

    @JsonProperty("status")
    public abstract int statusCode();
}
