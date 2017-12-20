package me.philippheuer.twitch4j.impl.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.philippheuer.twitch4j.models.IRestError;

@Data
@AllArgsConstructor
public class RestError implements IRestError{
    private final String error;
    private final String message;
    private final int statusCode;
}
