package twitch4j.helix;

import com.github.philippheuer.events4j.EventManager;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import twitch4j.helix.interceptors.CommonHeaderInterceptor;

/**
 * Twitch API v5
 *
 * Documentation: https://dev.twitch.tv/docs/v5/
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TwitchHelixBuilder {

    /**
     * Event Manager
     */
    @Wither
    private EventManager eventManager;

    /**
     * Http Client
     */
    private final OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * Client Id
     */
    @Wither
    private String clientId = "jzkbprff40iqj646a697cyrvl0zt2m6";

    /**
     * Client Secret
     */
    @Wither
    private String clientSecret = "**SECRET**";

    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36";

    /**
     * BaseUrl
     */
    private String baseUrl = "https://api.twitch.tv/helix";

    /**
     * Initialize the builder
     * @return Twitch Helix Builder
     */
    public static TwitchHelixBuilder builder() {
        return new TwitchHelixBuilder();
    }

    /**
     * Twitch API Client (Helix)
     * @return TwitchHelix
     */
    public TwitchHelix build() {
        TwitchHelix client = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .errorDecoder(new TwitchHelixErrorDecoder(new JacksonDecoder()))
                .logLevel(Logger.Level.BASIC)
                .requestInterceptor(new CommonHeaderInterceptor(this))
                .retryer(new Retryer.Default(1, 100, 3))
                .target(TwitchHelix.class, baseUrl);
        return client;
    }
}
