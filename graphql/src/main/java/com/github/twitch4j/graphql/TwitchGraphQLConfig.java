package com.github.twitch4j.graphql;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Accessors(chain = true, fluent = true, prefix = "")
@Setter
@Getter
public class TwitchGraphQLConfig {
    /**
     * The official base URL used by production Twitch Helix API servers.
     */
    public static final String OFFICIAL_BASE_URL = "https://gql.twitch.tv/gql";

    public static TwitchGraphQLConfig process(Consumer<TwitchGraphQLConfig> spec) {
        TwitchGraphQLConfig data = new TwitchGraphQLConfig();
        spec.accept(data);
        data.validate();
        return data;
    }

    /**
     * validate the config
     */
    public void validate() {
        Objects.requireNonNull(baseUrl, "baseUrl may not be null!");

        if (additionalHeaders == null) {
            additionalHeaders = Collections.emptyMap();
        }
    }

    /**
     * Client Id
     */
    private String clientId = Twitch4JGlobal.clientId;

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * BaseUrl
     */
    private String baseUrl = OFFICIAL_BASE_URL;

    /**
     * Default Timeout
     */
    private Integer timeout = 5000;

    /**
     * Proxy Configuration
     */
    private ProxyConfig proxyConfig = null;

    /**
     * Extra user-defined headers on each request
     */
    private Map<String, String> additionalHeaders;

    /**
     * Whether GraphQL Queries should be batched
     */
    private boolean batchingEnabled = false;

    /**
     * Default Token
     */
    private OAuth2Credential defaultToken = null;

}
