package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PubSubListenResponseEvent extends TwitchEvent {

    /**
     * The nonce that was passed in the request, if one was provided there.
     */
    String nonce;

    /**
     * The error message associated with the request, or an empty string if there is no error.
     */
    String error;

    public boolean hasError() {
        return error != null && error.length() > 0;
    }

    public boolean isBadAuthentication() {
        return "ERR_BADAUTH".equals(error);
    }

    public boolean isBadMessage() {
        return "ERR_BADMESSAGE".equals(error);
    }

    public boolean isBadTopic() {
        return "ERR_BADTOPIC".equals(error);
    }

    public boolean isServerError() {
        return "ERR_SERVER".equals(error);
    }

}
