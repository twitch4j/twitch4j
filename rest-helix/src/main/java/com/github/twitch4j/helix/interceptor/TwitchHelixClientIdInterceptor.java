package com.github.twitch4j.helix.interceptor;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Injects ClientId Header, the User Agent and other common headers into each API Request
 */
@Slf4j
public class TwitchHelixClientIdInterceptor implements RequestInterceptor {

    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Empirically determined rate limit on helix bans and unbans, per channel
     */
    @Unofficial
    private static final Bandwidth BANS_BANDWIDTH = Bandwidth.simple(100, Duration.ofSeconds(30));

    /**
     * Empirically determined rate limit on the helix create clip endpoint, per user
     */
    @Unofficial
    private static final Bandwidth CLIPS_BANDWIDTH = Bandwidth.simple(600, Duration.ofSeconds(60));

    /**
     * Empirically determined rate limit on helix add and remove block term, per channel
     */
    @Unofficial
    private static final Bandwidth TERMS_BANDWIDTH = Bandwidth.simple(60, Duration.ofSeconds(60));

    /**
     * Reference to the Client Builder
     */
    private final TwitchHelixBuilder twitchAPIBuilder;

    /**
     * Helix Rate Limit
     */
    private final Bandwidth apiRateLimit;

    /**
     * Reference to the twitch identity provider
     */
    @Setter
    private TwitchIdentityProvider twitchIdentityProvider;

    /**
     * Access token cache
     */
    @Getter(value = AccessLevel.PROTECTED)
    private final Cache<String, OAuth2Credential> accessTokenCache = Caffeine.newBuilder()
        .expireAfterAccess(15, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .build();

    /**
     * Rate limit buckets by user/app
     */
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Moderation API: ban and unban rate limit buckets per channel
     */
    private final Cache<String, Bucket> bansByChannelId = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Create Clip API rate limit buckets per user
     */
    private final Cache<String, Bucket> clipsByUserId = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * Moderation API: add and remove blocked term rate limit buckets per channel
     */
    private final Cache<String, Bucket> termsByChannelId = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.MINUTES)
        .build();

    /**
     * The default app access token that is used if no oauth was passed by the user
     */
    private volatile OAuth2Credential defaultAuthToken;

    /**
     * The default client id, typically associated with {@link TwitchHelixClientIdInterceptor#defaultAuthToken}
     */
    private volatile String defaultClientId;

    /**
     * Constructor
     *
     * @param twitchHelixBuilder Twitch Client Builder
     */
    public TwitchHelixClientIdInterceptor(TwitchHelixBuilder twitchHelixBuilder) {
        this.twitchAPIBuilder = twitchHelixBuilder;
        twitchIdentityProvider = new TwitchIdentityProvider(twitchHelixBuilder.getClientId(), twitchHelixBuilder.getClientSecret(), null);
        this.defaultClientId = twitchAPIBuilder.getClientId();
        this.apiRateLimit = twitchAPIBuilder.getApiRateLimit();
        this.defaultAuthToken = twitchHelixBuilder.getDefaultAuthToken();
        if (defaultAuthToken != null)
            twitchIdentityProvider.getAdditionalCredentialInformation(defaultAuthToken).ifPresent(oauth -> {
                this.defaultClientId = (String) oauth.getContext().get("client_id");
                accessTokenCache.put(oauth.getAccessToken(), oauth);
            });
    }

    /**
     * Interceptor
     *
     * @param template Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate template) {
        String clientId = this.defaultClientId;

        // if a oauth token is passed is has to match that client id, default to global client id otherwise (for ie. token verification)
        if (template.headers().containsKey(AUTH_HEADER)) {
            String oauthToken = template.headers().get(AUTH_HEADER).iterator().next().substring(BEARER_PREFIX.length());

            if (oauthToken.isEmpty()) {
                String clientSecret = twitchAPIBuilder.getClientSecret();
                if (defaultAuthToken == null && (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(clientSecret) || clientSecret.charAt(0) == '*'))
                    throw new RuntimeException("Necessary OAuth token was missing from Helix call, without the means to generate one!");

                try {
                    oauthToken = getOrCreateAuthToken().getAccessToken();
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate an app access token as no oauth token was passed to this Helix call", e);
                }

                template.removeHeader(AUTH_HEADER);
                template.header(AUTH_HEADER, BEARER_PREFIX + oauthToken);
            } else if (!StringUtils.contains(oauthToken, '.')) {
                OAuth2Credential verifiedCredential = accessTokenCache.getIfPresent(oauthToken);
                if (verifiedCredential == null) {
                    log.debug("Getting matching client-id for authorization token {}", oauthToken.substring(0, 5));

                    Optional<OAuth2Credential> requestedCredential = twitchIdentityProvider.getAdditionalCredentialInformation(new OAuth2Credential("twitch", oauthToken));
                    if (!requestedCredential.isPresent()) {
                        throw new RuntimeException("Failed to get the client_id for the provided authentication token, the authentication token may be invalid!");
                    }

                    verifiedCredential = requestedCredential.get();
                    accessTokenCache.put(oauthToken, verifiedCredential);
                }

                clientId = (String) verifiedCredential.getContext().get("client_id");
            }

            log.debug("Setting new client-id {} for token {}", clientId, oauthToken.substring(0, 5));
        }

        // set headers
        if (!template.headers().containsKey("Client-Id"))
            template.header("Client-Id", clientId);
        template.header("User-Agent", twitchAPIBuilder.getUserAgent());
    }

    public void updateRemaining(String token, int remaining) {
        this.updateRemainingGeneric(token, remaining, this::getKey, this::getOrInitializeBucket);
    }

    public void updateRemainingCreateClip(String token, int remaining) {
        this.updateRemainingGeneric(token, remaining, OAuth2Credential::getUserId, this::getClipBucket);
    }

    public void clearDefaultToken() {
        this.defaultAuthToken = null;
    }

    protected String getKey(OAuth2Credential credential) {
        String clientId = (String) credential.getContext().get("client_id");
        return clientId == null ? null : credential.getUserId() == null ? clientId : clientId + "-" + credential.getUserId();
    }

    protected Bucket getOrInitializeBucket(String key) {
        return buckets.get(key, k -> Bucket.builder().addLimit(this.apiRateLimit).build());
    }

    public Bucket getModerationBucket(String channelId) {
        return bansByChannelId.get(channelId, k -> Bucket.builder().addLimit(BANS_BANDWIDTH).build());
    }

    protected Bucket getClipBucket(String userId) {
        return clipsByUserId.get(userId, k -> Bucket.builder().addLimit(CLIPS_BANDWIDTH).build());
    }

    protected Bucket getTermsBucket(String channelId) {
        return termsByChannelId.get(channelId, k -> Bucket.builder().addLimit(TERMS_BANDWIDTH).build());
    }

    private OAuth2Credential getOrCreateAuthToken() {
        if (defaultAuthToken == null)
            synchronized (this) {
                if (defaultAuthToken == null) {
                    String clientId = twitchAPIBuilder.getClientId();
                    OAuth2Credential token = twitchIdentityProvider.getAppAccessToken();
                    token.getContext().put("client_id", clientId);
                    getOrInitializeBucket(clientId);
                    accessTokenCache.put(token.getAccessToken(), token);
                    this.defaultClientId = clientId;
                    return this.defaultAuthToken = token;
                }
            }

        return this.defaultAuthToken;
    }

    private void updateRemainingGeneric(String token, int remaining, Function<OAuth2Credential, String> credToKey, Function<String, Bucket> keyToBucket) {
        OAuth2Credential credential = accessTokenCache.getIfPresent(token);
        if (credential == null) return;

        String key = credToKey.apply(credential);
        if (key == null) return;

        Bucket bucket = keyToBucket.apply(key);
        long diff = bucket.getAvailableTokens() - remaining;
        if (diff > 0) bucket.tryConsumeAsMuchAsPossible(diff);
    }

}
