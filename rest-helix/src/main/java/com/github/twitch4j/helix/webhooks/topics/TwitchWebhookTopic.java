package com.github.twitch4j.helix.webhooks.topics;

import com.github.twitch4j.helix.webhooks.domain.WebhookNotification;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@EqualsAndHashCode
public abstract class TwitchWebhookTopic<T> {

    // Helix base URL
	private static final String BASE_URL = "https://api.twitch.tv/helix";

    // The topic URL, returned by toString()
	private final String url;

    /**
     * The data class that notifications for this topic deserialize to
     */
	@Getter
	private Class<T> type;

    /**
     * Create a new topic starting with "https://api.twitch.tv/helix"
     *
     * @param path The path to the specific Helix API endpoint..
     * @param type The data class that notifications for this topic deserialize to.
     * @param queryParameters A list of the query parameters for this topic URL.
     *                        Will be sorted alphabetically, so performance will be higher if it is already sorted.
     */
	public TwitchWebhookTopic(String path, Class<T> type, SortedMap<String, Object> queryParameters) {
	    this(BASE_URL, path, type, queryParameters);
    }

    /**
     * Override the base URL in case Twitch ever changes it or creates a new endpoint with a different URL.
     *
     * @param baseUrl The base URL of the endpoint.
     * @param path The path to the specific API endpoint.
     * @param type The data class that notifications for this topic deserialize to.
     * @param queryParameters A list of the query parameters for this topic URL.
     *                        Will be sorted alphabetically, so performance will be higher if it is already sorted.
     */
	public TwitchWebhookTopic(String baseUrl, String path, Class<T> type, SortedMap<String, Object> queryParameters) {
		this.type = type;
        this.url = baseUrl + path + buildQuery(queryParameters.entrySet());
	}

    /**
     * Create a new topic from an existing URL
     *
     * @param url The URL representing this topic.
     * @param type The data class that notifications for this topic deserialize to.
     */
	public TwitchWebhookTopic(String url, Class<T> type) {
	    this.url = url;
	    this.type = type;
    }

	// Generate the query string from the sorted list of parameters
    private String buildQuery(Iterable<Map.Entry<String, Object>> params) {
        StringBuilder urlBuilder = new StringBuilder();

        if(params != null) {
            boolean first = true;
            for(Map.Entry<String, Object> param : params) {
                if(param.getValue() != null) {
                    urlBuilder
                        .append(first ? "?" : "&")
                        .append(param.getKey())
                        .append("=")
                        .append(param.getValue().toString());
                    first = false;
                }
            }
        }

        return urlBuilder.toString();
    }

    /**
     * @return The URL associated with this topic
     */
    @Override
    public String toString() {
        return url;
    }

    public static TwitchWebhookTopic<?> fromUrl(String url) throws URISyntaxException {
        if(url.startsWith(BASE_URL)) {
            URI uri = new URI(url);
            String[] splitQuery = uri.getRawQuery().split("&");
            Map<String, String> params = new TreeMap<>();
            for(String s : splitQuery) {
                String[] splitParam = s.split("=");
                params.put(splitParam[0], splitParam[1]);
            }
            switch(uri.getPath().replaceFirst("/helix", "")) {
                case(ChannelBanTopic.PATH): {
                    String broadcasterId = params.get("broadcaster_id");
                    String userId = params.get("user_id");
                    return new ChannelBanTopic(broadcasterId, userId);
                }
                case(ChannelSubscriptionTopic.PATH): {
                    String broadcasterId = params.get("broadcaster_id");
                    String userId = params.get("user_id");
                    return new ChannelSubscriptionTopic(broadcasterId, userId);
                }
                case(ExtensionTransactionsTopic.PATH): {
                    String extensionId = params.get("extension_id");
                    return new ExtensionTransactionsTopic(extensionId);
                }
                case(FollowsTopic.PATH): {
                    String fromId = params.get("from_id");
                    String toId = params.get("to_id");
                    return new FollowsTopic(fromId, toId);
                }
                case(HypeTrainTopic.PATH): {
                    String broadcasterId = params.get("broadcaster_id");
                    return new HypeTrainTopic(broadcasterId);
                }
                case(ModeratorChangeTopic.PATH): {
                    String broadcasterId = params.get("broadcaster_id");
                    String userId = params.get("user_id");
                    return new ModeratorChangeTopic(broadcasterId, userId);
                }
                case(StreamsTopic.PATH): {
                    String userId = params.get("user_id");
                    return new StreamsTopic(userId);
                }
                case(UsersTopic.PATH): {
                    String userId = params.get("user_id");
                    return new UsersTopic(userId);
                }
            }
        }
        return new UnknownTopic(url);
    }

    public static class UnknownTopic extends TwitchWebhookTopic<WebhookNotification> {

        public UnknownTopic(String url) {
            super(url, WebhookNotification.class);
        }
    }

}
