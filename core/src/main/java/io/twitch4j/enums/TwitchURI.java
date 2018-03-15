package io.twitch4j.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TwitchURI {
    WEB_AUTH("https://id.twitch.tv"),
    WEB_KRAKEN("https://api.twitch.tv/kraken"),
    WEB_HELIX("https://api.twitch.tv/helix"),
    IRC_WS("wss://tmi-ws.chat.twitch.tv"),
    PUBSUB("wss://pubsub-edge.twitch.tv"),
    WEBSUB(WEB_HELIX.uri + "/webhooks/hub");

    private final String uri;

    public String buildUrl(String endpoint) {
        if (!endpoint.startsWith("/")) endpoint = "/" + endpoint;
        return uri + endpoint;
    }
}
