package com.github.twitch4j.pubsub.events;

import com.github.twitch4j.common.events.TwitchEvent;
import com.github.twitch4j.pubsub.domain.PubSubRequest;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;

import java.util.Optional;
import java.util.function.Supplier;

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

    @ToString.Exclude
    @Getter(AccessLevel.PRIVATE)
    Supplier<PubSubRequest> listenRequestSupplier;

    /**
     * @return the listen request associated with this response.
     * @implNote The current implementation requires unique nonce's across requests (which is the default behavior for the listen methods provided by the library).
     * @implSpec This method involves an O(n) operation where n <= 50, so it is best to only call it once when needed.
     */
    public Optional<PubSubRequest> getListenRequest() {
        return Optional.ofNullable(listenRequestSupplier.get());
    }

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
