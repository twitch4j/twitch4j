module com.github.twitch4j.chat {
    exports com.github.twitch4j.chat;
    exports com.github.twitch4j.chat.util;
    exports com.github.twitch4j.chat.events.channel;
    requires transitive lombok;
    requires transitive org.jetbrains.annotations;
    requires com.github.twitch4j.common;
    requires org.apache.commons.lang3;
    requires credentialmanager;
    requires io.github.bucket4j.core;
    requires events4j.core;
    requires events4j.api;
    requires com.github.twitch4j.auth;
    requires events4j.handler.simple;
    requires nv.websocket.client;
}
