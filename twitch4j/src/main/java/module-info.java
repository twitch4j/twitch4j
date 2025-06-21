@SuppressWarnings("deprecation")
module com.github.twitch4j {
    requires transitive lombok;
    requires transitive org.jetbrains.annotations;
    requires com.github.twitch4j.common;
    requires com.github.twitch4j.chat;
    requires credentialmanager;
    requires events4j.api;
    requires events4j.core;
    requires events4j.handler.simple;
    requires com.github.twitch4j.auth;
    requires com.github.twitch4j.pubsub;
    requires com.github.twitch4j.kraken;
    requires com.github.twitch4j.helix;
    requires com.github.twitch4j.graphql;
    requires com.github.twitch4j.extensions;
    requires com.github.twitch4j.messaginginterface;
    requires feign.core;
    requires io.github.bucket4j.core;
    requires org.apache.commons.lang3;
    requires com.github.benmanes.caffeine;
    requires hystrix.core;
    requires org.apache.commons.io;
}
