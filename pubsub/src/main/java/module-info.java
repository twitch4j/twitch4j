module com.github.twitch4j.pubsub {
    exports com.github.twitch4j.pubsub;
    requires transitive lombok;
    requires com.github.twitch4j.common;
    requires org.apache.commons.lang3;
    requires com.github.benmanes.caffeine;
    requires events4j.core;
    requires events4j.api;
    requires events4j.handler.simple;
    requires nv.websocket.client;
    requires com.fasterxml.jackson.databind;
    requires credentialmanager;
    requires transitive org.jetbrains.annotations;
    requires com.github.twitch4j.eventsub;
}
