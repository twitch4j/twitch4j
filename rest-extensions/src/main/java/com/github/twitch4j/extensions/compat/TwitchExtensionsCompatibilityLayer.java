package com.github.twitch4j.extensions.compat;

import com.github.twitch4j.common.config.ProxyConfig;
import com.github.twitch4j.common.config.Twitch4JGlobal;
import com.github.twitch4j.extensions.TwitchExtensions;
import com.github.twitch4j.extensions.domain.ChannelList;
import com.github.twitch4j.extensions.domain.ConfigurationSegment;
import com.github.twitch4j.extensions.domain.ConfigurationSegmentType;
import com.github.twitch4j.extensions.domain.ExtensionConfigurationSegment;
import com.github.twitch4j.extensions.domain.ExtensionInformation;
import com.github.twitch4j.extensions.domain.ExtensionSecretList;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.TwitchHelixBuilder;
import com.github.twitch4j.helix.domain.ExtensionConfigurationSegmentInput;
import com.github.twitch4j.helix.domain.ExtensionSegment;
import com.github.twitch4j.helix.domain.SendPubSubMessageInput;
import com.netflix.hystrix.HystrixCommand;
import feign.Logger;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static com.github.twitch4j.extensions.compat.ExtensionsTypeConverters.*; // Must be wildcard import for static: https://github.com/projectlombok/lombok/issues/2044

/**
 * Forwards twitch extensions api calls to the new helix api
 */
@SuppressWarnings("deprecation")
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public final class TwitchExtensionsCompatibilityLayer implements TwitchExtensions {

    private final String clientId;
    private final TwitchHelix helix;

    @Builder
    public TwitchExtensionsCompatibilityLayer(String clientId, String clientSecret, String userAgent, Long timeout, Integer requestQueueSize, Logger.Level logLevel, ProxyConfig proxyConfig) {
        this.clientId = clientId;
        this.helix = TwitchHelixBuilder.builder()
            .withClientId(clientId)
            .withClientSecret(clientSecret)
            .withUserAgent(userAgent != null ? userAgent : Twitch4JGlobal.userAgent)
            .withTimeout(timeout != null ? timeout.intValue() : 5000)
            .withRequestQueueSize(requestQueueSize != null ? requestQueueSize : -1)
            .withLogLevel(logLevel != null ? logLevel : Logger.Level.NONE)
            .withProxyConfig(proxyConfig)
            .build();
    }

    @Override
    public HystrixCommand<ExtensionSecretList> createExtensionSecret(String clientId, String jsonWebToken, int activationDelaySeconds) {
        return new HystrixCommandConverter<>(
            helix.createExtensionSecret(jsonWebToken, getClientId(clientId), activationDelaySeconds),
            SECRETS_CONVERTER
        );
    }

    @Override
    public HystrixCommand<ExtensionSecretList> getExtensionSecret(String clientId, String jsonWebToken) {
        return new HystrixCommandConverter<>(
            helix.getExtensionSecrets(jsonWebToken, getClientId(clientId)),
            SECRETS_CONVERTER
        );
    }

    @Override
    public HystrixCommand<Void> revokeExtensionSecrets(String clientId, String jsonWebToken) {
        throw new UnsupportedOperationException("There is no direct Helix replacement for this endpoint.");
    }

    @Override
    public HystrixCommand<ChannelList> getLiveChannelsWithExtensionActivated(String clientId, String cursor) {
        return new HystrixCommandConverter<>(
            helix.getExtensionLiveChannels(null, getClientId(clientId), 100, cursor),
            LIVE_CHANNELS_CONVERTER
        );
    }

    @Override
    public HystrixCommand<Void> setExtensionRequiredConfiguration(String clientId, String jsonWebToken, String extensionVersion, String channelId, String requiredConfiguration) {
        return helix.setExtensionRequiredConfiguration(
            jsonWebToken,
            getClientId(clientId),
            extensionVersion,
            requiredConfiguration,
            channelId
        );
    }

    @Override
    public HystrixCommand<Void> setExtensionConfigurationSegment(String clientId, String jsonWebToken, ExtensionConfigurationSegment configurationSegment) {
        final String extensionId = getClientId(clientId);
        return helix.setExtensionConfigurationSegment(
            jsonWebToken,
            extensionId,
            ExtensionConfigurationSegmentInput.builder()
                .extensionId(extensionId)
                .segment(SEGMENT_CONVERTER.apply(configurationSegment.getSegment()))
                .broadcasterId(configurationSegment.getChannelId())
                .content(configurationSegment.getContent())
                .version(configurationSegment.getVersion())
                .build()
        );
    }

    @Override
    public HystrixCommand<Map<String, ConfigurationSegment>> getExtensionChannelConfiguration(String clientId, String jsonWebToken, String channelId) {
        return new HystrixCommandConverter<>(
            helix.getExtensionConfigurationSegment(jsonWebToken, getClientId(clientId), Arrays.asList(ExtensionSegment.BROADCASTER, ExtensionSegment.DEVELOPER), channelId),
            CONFIG_SEGMENT_LIST_CONVERTER
        );
    }

    @Override
    public HystrixCommand<Map<String, ConfigurationSegment>> getExtensionConfigurationSegment(String clientId, String jsonWebToken, ConfigurationSegmentType segmentType, String channelId) {
        return new HystrixCommandConverter<>(
            helix.getExtensionConfigurationSegment(jsonWebToken, getClientId(clientId), Collections.singletonList(SEGMENT_CONVERTER.apply(segmentType)), channelId),
            CONFIG_SEGMENT_LIST_CONVERTER
        );
    }

    @Override
    public HystrixCommand<Void> sendExtensionPubSubMessage(String clientId, String jsonWebToken, String channelId, String message, String targets) {
        return helix.sendExtensionPubSubMessage(
            jsonWebToken,
            getClientId(clientId),
            SendPubSubMessageInput.builder()
                .broadcasterId(channelId)
                .message(message)
                .target(targets)
                .globalBroadcast(SendPubSubMessageInput.GLOBAL_TARGET.equals(targets))
                .build()
        );
    }

    @Override
    public HystrixCommand<Void> sendExtensionChatMessage(String clientId, String jsonWebToken, String extensionVersion, String channelId, String text) {
        return helix.sendExtensionChatMessage(
            jsonWebToken,
            getClientId(clientId),
            extensionVersion,
            channelId,
            text
        );
    }

    @Override
    public HystrixCommand<ExtensionInformation> getExtensionInformation(String clientId) {
        return new HystrixCommandConverter<>(
            helix.getReleasedExtensions(null, getClientId(clientId), null),
            EXTENSION_LIST_CONVERTER
        );
    }

    private String getClientId(String overrideId) {
        return StringUtils.isNotBlank(overrideId) ? overrideId : this.clientId;
    }

}
