package com.github.twitch4j.socket;

import com.github.philippheuer.credentialmanager.CredentialManager;
import com.github.philippheuer.credentialmanager.CredentialManagerBuilder;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.auth.providers.TwitchIdentityProvider;
import com.github.twitch4j.common.pool.TwitchModuleConnectionPool;
import com.github.twitch4j.eventsub.EventSubSubscription;
import com.github.twitch4j.eventsub.condition.ChannelEventSubCondition;
import com.github.twitch4j.eventsub.condition.UserEventSubCondition;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

@SuperBuilder
public final class TwitchEventSocketPool extends TwitchModuleConnectionPool<TwitchEventSocket, EventSubSubscription, EventSubSubscription, Boolean, TwitchEventSocket.TwitchEventSocketBuilder> implements IEventSubSocket {

    private final String threadPrefix = "twitch4j-pool-" + RandomStringUtils.random(4, true, true) + "-eventsub-ws-";

    @Builder.Default
    public String baseUrl = TwitchEventSocket.WEB_SOCKET_SERVER;

    @Nullable
    @Builder.Default
    private TwitchHelix helix = TwitchHelixBuilder.builder().build();

    @Nullable
    @Builder.Default
    @Getter
    private OAuth2Credential defaultToken = null;

    @NotNull
    @Builder.Default
    private Function<EventSubSubscription, OAuth2Credential> tokenForSubscription = x -> null;

    @NotNull
    @Builder.Default
    private CredentialManager credentialManager = CredentialManagerBuilder.builder().build();

    private final Cache<EventSubSubscription, OAuth2Credential> credentials = CacheApi.create(spec -> {
        spec.maxSize(4096L);
        spec.expiryType(ExpiryType.POST_WRITE);
        spec.expiryTime(Duration.ofMinutes(5L));
    });

    @Override
    protected TwitchEventSocket createConnection() {
        return advancedConfiguration.apply(
            TwitchEventSocket.builder()
                .api(helix)
                .baseUrl(baseUrl)
                .defaultToken(defaultToken)
                .eventManager(getConnectionEventManager())
                .proxyConfig(proxyConfig.get())
                .taskExecutor(getExecutor(threadPrefix + RandomStringUtils.random(4, true, true), TwitchEventSocket.REQUIRED_THREAD_COUNT))
        ).build();
    }

    @Override
    @SneakyThrows
    protected void disposeConnection(TwitchEventSocket connection) {
        connection.close();
    }

    @Override
    protected EventSubSubscription handleSubscription(TwitchEventSocket twitchEventSocket, EventSubSubscription eventSubSubscription) {
        OAuth2Credential cred = credentials.remove(eventSubSubscription);
        boolean success = twitchEventSocket.register(cred != null ? cred : getTokenForSub(eventSubSubscription), eventSubSubscription);
        if (success) {
            return twitchEventSocket.getSubscriptions().stream()
                .filter(sub -> TwitchEventSocket.EQUALS.test(sub, eventSubSubscription))
                .findAny()
                .orElse(eventSubSubscription);
        }
        return null;
    }

    @Override
    protected EventSubSubscription handleDuplicateSubscription(TwitchEventSocket twitchEventSocket, TwitchEventSocket old, EventSubSubscription eventSubSubscription) {
        return twitchEventSocket != null && twitchEventSocket != old && twitchEventSocket.unregister(eventSubSubscription) ? eventSubSubscription : null;
    }

    @Override
    protected Boolean handleUnsubscription(TwitchEventSocket twitchEventSocket, EventSubSubscription eventSubSubscription) {
        return twitchEventSocket.unregister(eventSubSubscription);
    }

    @Override
    protected EventSubSubscription getRequestFromSubscription(EventSubSubscription eventSubSubscription) {
        return eventSubSubscription;
    }

    @Override
    protected int getSubscriptionSize(EventSubSubscription eventSubSubscription) {
        return 1;
    }

    @Override
    public void connect() {
        if (saturatedConnections.isEmpty() && unsaturatedConnections.isEmpty()) {
            unsaturatedConnections.put(createConnection(), 0);
        }
    }

    @Override
    public void disconnect() {
        getConnections().forEach(TwitchEventSocket::disconnect);
    }

    @Override
    public void reconnect() {
        getConnections().forEach(TwitchEventSocket::reconnect);
    }

    @Override
    public Collection<EventSubSubscription> getSubscriptions() {
        return Collections.unmodifiableSet(subscriptions.keySet());
    }

    @Override
    public boolean register(OAuth2Credential token, EventSubSubscription sub) {
        SubscriptionWrapper wrapped = new SubscriptionWrapper(sub);
        credentials.put(wrapped, token);
        return subscribe(wrapped) != null;
    }

    @Override
    public boolean unregister(EventSubSubscription sub) {
        return this.unsubscribe(new SubscriptionWrapper(sub));
    }

    private OAuth2Credential getTokenForSub(EventSubSubscription sub) {
        OAuth2Credential token = tokenForSubscription.apply(SubscriptionWrapper.wrap(sub));
        if (token != null)
            return token;

        String targetId;
        if (sub.getCondition() instanceof ChannelEventSubCondition) {
            targetId = ((ChannelEventSubCondition) sub.getCondition()).getBroadcasterUserId();
        } else if (sub.getCondition() instanceof UserEventSubCondition) {
            targetId = ((UserEventSubCondition) sub.getCondition()).getUserId();
        } else {
            targetId = null;
        }

        if (defaultToken != null && targetId == null)
            return defaultToken;

        return credentialManager.getCredentials().stream()
            .filter(cred -> TwitchIdentityProvider.PROVIDER_NAME.equalsIgnoreCase(cred.getIdentityProvider()))
            .filter(cred -> StringUtils.isNotEmpty(cred.getUserId()))
            .filter(cred -> cred instanceof OAuth2Credential)
            .map(cred -> (OAuth2Credential) cred)
            .filter(cred -> targetId == null || cred.getUserId().equals(targetId))
            .findAny()
            .orElse(defaultToken);
    }
}
