package com.github.twitch4j.pubsub.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class AutomodCaughtMessage {

    private Content content;
    private String id;
    private Sender sender;
    private Instant sentAt;

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Content {
        private String text;
        private List<Fragment> fragments;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Fragment {
        private String text;
        private FragmentFlags automod;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class FragmentFlags {
        private Map<String, Integer> topics;
    }

    @Data
    @Setter(AccessLevel.PRIVATE)
    public static class Sender {
        private String userId;
        private String login;
        private String displayName;
        private String chatColor;
        private List<Badge> badges;

        @Data
        @Setter(AccessLevel.PRIVATE)
        public static class Badge {
            private String id;
            private String version;
        }
    }

}
