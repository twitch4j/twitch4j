package me.philippheuer.twitch4j.events.event.pubsub;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;
import me.philippheuer.twitch4j.events.Event;
import org.apache.commons.lang3.StringUtils;

@Value
@Getter
@EqualsAndHashCode(callSuper = false)
public class ResponseEvent extends Event {
    private final RuntimeException error;
    private final String nonce;

    public ResponseEvent(JsonNode data) {
        this.nonce = data.get("nonce").toString();
        String err = data.get("error").toString();
        if (StringUtils.isNotEmpty(err)) {
            switch (err) {
                case "ERR_BADMESSAGE":
                    this.error = new RuntimeException("Wrong format message.");
                    break;
                case "ERR_BADAUTH":
                    this.error = new RuntimeException("Wrong OAuth key for specify topic (user id).");
                    break;
                case "ERR_SERVER":
                    this.error = new RuntimeException("Server Error.");
                    break;
                case "ERR_BADTOPIC":
                    this.error = new RuntimeException("Wrong topic.");
                    break;
                default:
                    this.error = new RuntimeException(err);
                    break;
            }
        } else this.error = null;

    }
}
