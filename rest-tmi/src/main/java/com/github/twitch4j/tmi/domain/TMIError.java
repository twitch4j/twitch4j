package com.github.twitch4j.tmi.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@Getter
public class TMIError {

    /**
     * Error Type
     */
    private String error;

    /**
     * Error Code
     */
    private String status;

    /**
     * Error Message
     */
    private String message;

}
