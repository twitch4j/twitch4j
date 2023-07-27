package com.github.twitch4j.graphql;

import com.github.twitch4j.common.config.ProxyConfig;
import com.netflix.config.ConfigurationManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

/**
 * A factory to build instances of {@link TwitchGraphQL}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@ApiStatus.Experimental
public class TwitchGraphQLFactory {

    /**
     * Creates a new {@link TwitchGraphQL} instance.
     * <p>
     * This method allows the creation of validated {@link TwitchGraphQL} instances using a consumer-based specification pattern.
     *
     * @param spec The specification to construct the {@link TwitchGraphQL} instance.
     * @return The created instance of {@link TwitchGraphQL}.
     */
    public static TwitchGraphQL create(Consumer<TwitchGraphQLFactorySpec> spec) {
        TwitchGraphQLFactorySpec config = new TwitchGraphQLFactorySpec(spec);

        // hystrix
        ConfigurationManager.getConfigInstance().setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", config.timeout());

        return new TwitchGraphQL(config.baseUrl(), config.userAgent(), null, config.clientId(), config.defaultFirstPartyToken(), ProxyConfig.ofProxySpec(config.proxy()), config.enableBatching(), config.timeout(), config.additionalHeaders());
    }

}
