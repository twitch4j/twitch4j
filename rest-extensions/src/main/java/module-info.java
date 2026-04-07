module com.github.twitch4j.extensions {
    exports com.github.twitch4j.extensions;
    requires transitive lombok;
    requires com.github.twitch4j.common;
    requires feign.core;
    requires archaius.core;
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires feign.hystrix;
    requires feign.okhttp;
    requires feign.jackson;
    requires feign.slf4j;
    requires hystrix.core;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
}
