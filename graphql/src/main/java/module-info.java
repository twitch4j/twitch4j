module com.github.twitch4j.graphql {
    exports com.github.twitch4j.graphql;
    requires transitive lombok;
    requires com.github.twitch4j.common;
    requires events4j.core;
    requires events4j.api;
    requires events4j.handler.simple;
    requires credentialmanager;
    requires archaius.core;
    requires apollo.runtime;
    requires com.github.benmanes.caffeine;
    requires okhttp3;
    requires transitive org.jetbrains.annotations;
    requires apollo.http.cache.api;
    requires hystrix.core;
}
