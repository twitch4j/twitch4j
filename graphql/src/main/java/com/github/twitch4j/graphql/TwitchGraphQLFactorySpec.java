package com.github.twitch4j.graphql;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.spec.ModuleSpec;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A specification to construct a {@link TwitchGraphQL} instance.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@ApiStatus.Experimental
public final class TwitchGraphQLFactorySpec extends ModuleSpec<TwitchGraphQLFactorySpec> {

    /**
     * The official base URL used by production Twitch GraphQL API.
     */
    public static final String OFFICIAL_BASE_URL = "https://gql.twitch.tv/gql";

    /**
     * The name of the backend
     */
    @NotNull
    private String backendName = "twitch-graphql";

    /**
     * The api base URL
     */
    @NotNull
    private String baseUrl = OFFICIAL_BASE_URL;

    /**
     * Client Id
     */
    @NotNull
    private String clientId = "kimne78kx3ncx6brgo4mv6wki5h1ko";

    /**
     * User Agent
     */
    @NotNull
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * Default First-Party OAuth Token
     */
    @Nullable
    private OAuth2Credential defaultFirstPartyToken = null;

    /**
     * Whether GraphQL Queries should be batched
     */
    private boolean enableBatching = false;

    /**
     * Default Timeout
     */
    @NotNull
    private Integer timeout = 5000;

    /**
     * Additional headers to be applied to each outbound request
     */
    @Nullable
    private Map<String, String> additionalHeaders = null;

    /**
     * Confirms that you are aware of the risks of using unofficial APIs.
     * <p>
     * GraphQL is a first-party API and not intended for third-party use.
     */
    private boolean useUnofficialApiAtMyOwnRisk = false;

    /**
     * Constructs a validated implementation of {@link TwitchGraphQLFactorySpec}.
     *
     * @param spec the specification to process
     */
    @ApiStatus.Internal
    public TwitchGraphQLFactorySpec(@NotNull Consumer<TwitchGraphQLFactorySpec> spec) {
        spec.accept(this);
        validate();
    }

    /**
     * Ensures the configured specification is valid.
     *
     * @throws NullPointerException if any of the required fields are {@code null}
     */
    public void validate() {
        Objects.requireNonNull(backendName, "backendName must not be null");
        Objects.requireNonNull(baseUrl, "baseUrl must not be null");
        if (baseUrl.isEmpty()) {
            throw new IllegalArgumentException("baseUrl must not be empty");
        }
        Objects.requireNonNull(clientId, "clientId must not be null");
        Objects.requireNonNull(userAgent, "userAgent must not be null");
        Objects.requireNonNull(timeout, "timeout must not be null");
        if (!useUnofficialApiAtMyOwnRisk) {
            throw new IllegalArgumentException("You are required to confirm that you are aware of the risks of using unofficial APIs. Confirm by setting useUnofficialApiAtMyOwnRisk(true).");
        }
    }
}
