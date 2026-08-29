module com.github.twitch4j.common {
    requires transitive lombok;
    requires credentialmanager;
    requires nv.websocket.client;
    requires okhttp3;
    requires com.fasterxml.jackson.annotation;
    requires events4j.core;
    requires events4j.handler.simple;
    requires org.apache.commons.lang3;
    requires feign.core;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires io.github.bucket4j.core;
    requires com.fasterxml.jackson.datatype.jsr310;

    exports com.github.twitch4j.common.util;
    exports com.github.twitch4j.common.annotation;
    exports com.github.twitch4j.common.enums;
    exports com.github.twitch4j.common.config;
    exports com.github.twitch4j.common.pool;
    exports com.github.twitch4j.common.events.domain;
    exports com.github.twitch4j.common.events.user;
    exports com.github.twitch4j.common.events;
    exports com.github.twitch4j.common.feign;
    exports com.github.twitch4j.common.exception;
    exports com.github.twitch4j.common.feign.interceptor;
    exports com.github.twitch4j.common.events.channel;
}
