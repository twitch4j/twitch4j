package com.github.twitch4j.pubsub.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class LowTrustUserNewMessage {

    @JsonProperty("low_trust_user")
    private LowTrustUser user;

    private Content messageContent;

    private String messageId;

    private Instant sentAt;

    @Data
    @Setter(AccessLevel.NONE)
    public static class Content {
        private String text;
        private List<Fragment> fragments;
    }

    @Data
    @Setter(AccessLevel.NONE)
    public static class Fragment {
        private String text;
    }

}
