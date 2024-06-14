module com.github.twitch4j.auth {
    requires static lombok;
    requires okhttp3;
    requires credentialmanager;
    requires com.fasterxml.jackson.databind;

    exports com.github.twitch4j.auth;
    exports com.github.twitch4j.auth.domain;
    exports com.github.twitch4j.auth.providers;
}
