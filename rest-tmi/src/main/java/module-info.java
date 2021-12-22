module com.github.twitch4j.messaginginterface {
    exports com.github.twitch4j.tmi;
    requires transitive lombok;
    requires transitive org.jetbrains.annotations;
    requires feign.core;
    requires com.fasterxml.jackson.databind;
    requires com.github.twitch4j.common;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires archaius.core;
    requires okhttp3;
    requires feign.okhttp;
    requires feign.jackson;
    requires feign.hystrix;
    requires feign.slf4j;
}
