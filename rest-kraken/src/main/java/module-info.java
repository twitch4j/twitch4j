@Deprecated
module com.github.twitch4j.kraken {
    exports com.github.twitch4j.kraken;
    requires transitive lombok;
    requires transitive org.jetbrains.annotations;
    requires feign.core;
    requires com.fasterxml.jackson.databind;
    requires com.github.twitch4j.common;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires archaius.core;
    requires feign.hystrix;
    requires feign.jackson;
    requires feign.okhttp;
    requires feign.slf4j;
    requires okhttp3;
    requires hystrix.core;
    requires com.github.twitch4j.auth;
    requires credentialmanager;
}
