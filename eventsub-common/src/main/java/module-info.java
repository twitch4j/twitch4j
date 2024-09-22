module com.github.twitch4j.eventsub {
    exports com.github.twitch4j.eventsub.domain;
    exports com.github.twitch4j.eventsub;
    requires transitive lombok;
    requires transitive org.jetbrains.annotations;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.github.twitch4j.common;
    requires com.github.benmanes.caffeine;
    requires org.apache.commons.lang3;
}
