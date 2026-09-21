module com.github.twitch4j.helix {
    exports com.github.twitch4j.helix;
    exports com.github.twitch4j.helix.domain;
    requires transitive lombok;
    requires transitive org.jetbrains.annotations;
    requires feign.core;
    requires com.fasterxml.jackson.databind;
    requires com.github.twitch4j.common;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires credentialmanager;
    requires archaius.core;
    requires okhttp3;
    requires feign.hystrix;
    requires feign.jackson;
    requires feign.okhttp;
    requires feign.slf4j;
    requires hystrix.core;
    requires com.github.twitch4j.eventsub;
    requires io.github.bucket4j.core;
    requires com.github.benmanes.caffeine;
    requires com.github.twitch4j.auth;
}
