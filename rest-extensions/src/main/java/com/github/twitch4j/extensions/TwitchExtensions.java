package com.github.twitch4j.extensions;

import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.extensions.domain.ChannelList;
import com.github.twitch4j.extensions.domain.ConfigurationSegment;
import com.github.twitch4j.extensions.domain.ConfigurationSegmentType;
import com.github.twitch4j.extensions.domain.ExtensionConfigurationSegment;
import com.github.twitch4j.extensions.domain.ExtensionInformation;
import com.github.twitch4j.extensions.domain.ExtensionSecretList;
import com.github.twitch4j.common.feign.JsonStringExpander;
import com.netflix.hystrix.HystrixCommand;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.jetbrains.annotations.ApiStatus;

import java.util.Map;

/**
 * Twitch - Extensions API
 *
 * @see <a href="https://discuss.dev.twitch.tv/t/how-extensions-are-affected-by-the-legacy-twitch-api-v5-shutdown/32708">Twitch Shutdown Announcement</a>
 * @deprecated the Extensions API traditionally uses the decommissioned Kraken API. While the module now forwards calls to Helix, please migrate to using Helix directly as this module will be removed in the future.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval(inVersion = "2.0.0")
public interface TwitchExtensions {

    /**
     * Creates a new secret for a specified extension, identified by a client ID value assigned to the extension when it is created.
     * Also rotates any current secrets out of service, with enough time for extension clients to gracefully switch over to the new secret.
     * <p>
     * Use this function only when you are ready to install the new secret it returns.
     * <p>
     * Note: You get the initial secret via the Twitch developer site. Use this endpoint to rotate the secret later, if desired.
     *
     * @param clientId               The extension's client ID.
     * @param jsonWebToken           Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions).
     * @param activationDelaySeconds The delay period, between the generation of the new secret and its use by Twitch, is specified by this required parameter. Default (and minimum): 300 (5 minutes).
     * @return ExtensionSecretList
     */
    @RequestLine("POST /{client_id}/auth/secret")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"activation_delay_secs\":{activation_delay_secs}%7D")
    HystrixCommand<ExtensionSecretList> createExtensionSecret(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        @Param("activation_delay_secs") int activationDelaySeconds
    );

    /**
     * Retrieves a specified extension’s secret data: a version and an array of secret objects.
     *
     * @param clientId     The extension is identified by a client ID value assigned to the extension when it is created.
     * @param jsonWebToken Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions).
     * @return ExtensionSecretList
     */
    @RequestLine("GET /{client_id}/auth/secret")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}"
    })
    HystrixCommand<ExtensionSecretList> getExtensionSecret(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken
    );

    /**
     * Deletes all secrets associated with a specified extension, identified by a client ID value assigned to the extension when it is created.
     * <p>
     * This immediately breaks all clients until both a new Create Extension Secret is executed and the clients manually refresh themselves.
     * Use this only if a secret is compromised and must be removed immediately from circulation.
     *
     * @param clientId     The client ID identifying the extension when it is created.
     * @param jsonWebToken Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions)
     * @return 204 No Content on a successful call
     * @deprecated No migration path in the new Helix API.
     */
    @RequestLine("DELETE /{client_id}/auth/secret")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}"
    })
    @Deprecated
    HystrixCommand<Void> revokeExtensionSecrets(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken
    );

    /**
     * Returns one page of live channels that have installed and activated a specified extension.
     * <p>
     * A channel that just went live may take a few minutes to appear in this list, and a channel may continue to appear on this list for a few minutes after it stops broadcasting.
     *
     * @param clientId The client ID value assigned to the extension when it is created.
     * @param cursor   Cursor for forward pagination.
     * @return ChannelList
     */
    @RequestLine("GET /{client_id}/live_activated_channels?cursor={cursor}")
    @Headers("Client-Id: {client_id}")
    HystrixCommand<ChannelList> getLiveChannelsWithExtensionActivated(
        @Param("client_id") String clientId,
        @Param("cursor") String cursor
    );

    /**
     * Returns one page of live channels that have installed and activated a specified extension.
     *
     * @param clientId The client ID value assigned to the extension when it is created.
     * @return ChannelList
     * @deprecated use {@link #getLiveChannelsWithExtensionActivated(String, String)} instead (can pass null for cursor)
     */
    @Deprecated
    default HystrixCommand<ChannelList> getLiveChannelsWithExtensionActivated(String clientId) {
        return getLiveChannelsWithExtensionActivated(clientId, null);
    }

    /**
     * Enable activation of a specified extension, after any required broadcaster configuration is correct.
     * <p>
     * This is for extensions that require broadcaster configuration before activation. Use this if, in extension Capabilities, you select Custom/My Own Service.
     * <p>
     * You enforce required broadcaster configuration with a required_configuration string in the extension manifest.
     * The contents of this string can be whatever you want.
     * Once your EBS determines that the extension is correctly configured on a channel, use this endpoint to provide that same configuration string, which enables activation on the channel.
     * <p>
     * If a future version of the extension requires a different configuration, change the required_configuration string in your manifest.
     * When the new version is released, broadcasters will be required to re-configure that new version.
     *
     * @param clientId              The extension is identified by a client ID value assigned to the extension when it is created.
     * @param jsonWebToken          Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions).
     * @param extensionVersion      The extension version.
     * @param channelId             The channel id for the broadcaster.
     * @param requiredConfiguration The required configuration.
     * @return 204 No Content on a successful call
     */
    @RequestLine("PUT /{client_id}/{extension_version}/required_configuration?channel_id={channel_id}")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"required_configuration\":\"{required_configuration}\"%7D")
    HystrixCommand<Void> setExtensionRequiredConfiguration(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        @Param("extension_version") String extensionVersion,
        @Param("channel_id") String channelId,
        @Param(value = "required_configuration", expander = JsonStringExpander.class) String requiredConfiguration
    );

    /**
     * Sets a single configuration segment (any type). The segment type is specified by a required body parameter.
     * <p>
     * Each segment is limited to 5 KB and can be set at most 20 times per minute. Updates to this data are not delivered to Extensions that have already rendered.
     * <p>
     * Note: Your signed JWT must include the exp, user_id, and role fields, documented in JWT Schema. The role value must be external.
     *
     * @param clientId             The client ID identifying the extension when it is created.
     * @param jsonWebToken         Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions).
     * @param configurationSegment The updated configuration segment.
     * @return 204 No Content on a successful call
     */
    @RequestLine("PUT /{client_id}/configurations")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    HystrixCommand<Void> setExtensionConfigurationSegment(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        ExtensionConfigurationSegment configurationSegment
    );

    /**
     * Gets the developer and broadcaster configuration segments for a specified channel.
     * <p>
     * Each channel can be retrieved at most 20 times per minute.
     * <p>
     * Note: Your signed JWT must include the exp, user_id, and role fields, documented in JWT Schema. The role value must be external.
     *
     * @param clientId     The client ID identifying the extension when it is created.
     * @param jsonWebToken Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions).
     * @param channelId    The channel id of the broadcaster.
     * @return Map
     */
    @RequestLine("GET /{client_id}/configurations/channels/{channel_id}")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}"
    })
    HystrixCommand<Map<String, ConfigurationSegment>> getExtensionChannelConfiguration(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        @Param("channel_id") String channelId
    );

    /**
     * Gets a broadcaster or developer configuration segment for a specified channel, or the global configuration segment.
     * <p>
     * Each segment can be retrieved at most 20 times per minute.
     * <p>
     * Note: Your signed JWT must include the exp, user_id, and role fields, documented in JWT Schema. The role value must be external.
     *
     * @param clientId     The client ID identifying the extension when it is created.
     * @param jsonWebToken Signed JWT created by the EBS, following the requirements documented in “Signing the JWT” (in Building Extensions).
     * @param segmentType  The segment type.
     * @param channelId    The channel id of the broadcaster (must be null for {@link ConfigurationSegmentType#GLOBAL}).
     * @return Map
     */
    @RequestLine("GET /{client_id}/configurations/segments/{segment_type}?channel_id={channel_id}")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}"
    })
    HystrixCommand<Map<String, ConfigurationSegment>> getExtensionConfigurationSegment(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        @Param("segment_type") ConfigurationSegmentType segmentType,
        @Param("channel_id") String channelId
    );

    /**
     * Twitch provides a publish-subscribe system for your EBS to communicate with both the broadcaster and viewers.
     * Calling this endpoint forwards your message using the same mechanism as the send JavaScript helper function.
     * A message can be sent to either a specified channel or globally (all channels on which your extension is active).
     * <p>
     * Note: Your signed JWT must include the channel_id and pubsub_perms fields, documented in <a href="https://dev.twitch.tv/docs/extensions/reference#jwt-schema">JWT Schema</a>.
     *
     * @param clientId     The client ID identifying the extension when it is created.
     * @param jsonWebToken Signed JWT (Twitch or EBS JWTs are allowed)
     * @param channelId    Either a specific channel's ID or "all"
     * @param message      Your message to be sent.
     * @param targets      Valid values: "broadcast", "global".
     * @return 204 No Content on a successful call
     */
    @RequestLine("POST /message/{channel_id}")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"content_type\":\"application/json\",\"message\":\"{message}\",\"targets\":[\"{targets}\"]%7D")
    HystrixCommand<Void> sendExtensionPubSubMessage(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        @Param("channel_id") String channelId,
        @Param(value = "message", expander = JsonStringExpander.class) String message,
        @Param(value = "targets") String targets // Cannot be treated as List<String> due to feign limitation (" is converted to %22 and Param#encoded is deprecated/ineffective)
    );

    /**
     * Sends a specified chat message to a specified channel. The message will appear in the channel’s chat as a normal message.
     * The “username” of the message is the extension name you specified when you created the extension.
     * <p>
     * There is a limit of 12 messages per minute, per channel.
     * Extension chat messages use the same rate-limiting functionality as the New Twitch API (see <a href="https://dev.twitch.tv/docs/api/guide/#rate-limits">Rate Limits</a>).
     * The maximum message size is 280 characters.
     * <p>
     * The channel_id inside the JWT must match the channel ID in the request URL.
     *
     * @param clientId         The client ID identifying the extension when it is created.
     * @param jsonWebToken     Signed JWT created by the EBS, following the requirements documented in Signing the JWT (in Building Extensions) or Twitch JWT containing the "broadcaster" role.
     * @param extensionVersion The extension version.
     * @param channelId        The id of the channel to send the message to.
     * @param text             The message to be sent.
     * @return 204 No Content on a successful call
     */
    @RequestLine("POST /{client_id}/{extension_version}/channels/{channel_id}/chat")
    @Headers({
        "Client-Id: {client_id}",
        "Authorization: Bearer {token}",
        "Content-Type: application/json"
    })
    @Body("%7B\"text\":\"{text}\"%7D")
    HystrixCommand<Void> sendExtensionChatMessage(
        @Param("client_id") String clientId,
        @Param("token") String jsonWebToken,
        @Param("extension_version") String extensionVersion,
        @Param("channel_id") String channelId,
        @Param(value = "text", expander = JsonStringExpander.class) String text
    );

    /**
     * Get Extension Information
     *
     * @param clientId The client ID value assigned to the extension when it is created.
     * @return ExtensionInformation
     * @see Unofficial
     */
    @Unofficial
    @RequestLine("GET /{client_id}/") // Trailing slash allows for the interceptor to inject a missing client id
    @Headers("Client-Id: {client_id}")
    HystrixCommand<ExtensionInformation> getExtensionInformation(
        @Param("client_id") String clientId
    );

}
