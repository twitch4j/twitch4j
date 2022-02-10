package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @deprecated Due endpoints is deprecated decommission have been planned on <b>Febuary 28, 2022</b>.
 *             More details about decommission finds <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>
 */
@Data
@Deprecated
@Setter(AccessLevel.PRIVATE)
@Getter
public class TwitchKrakenError {

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
