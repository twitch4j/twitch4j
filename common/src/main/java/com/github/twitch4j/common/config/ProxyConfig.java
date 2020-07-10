package com.github.twitch4j.common.config;

import com.neovisionaries.ws.client.ProxySettings;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Arrays;

/**
 * Wrapper around a HTTP(S) proxy to be used by Twitch4J modules
 */
@Data
@Builder
public class ProxyConfig {
    /*
     * Only HTTP(S) proxies are supported due to library limitations
     */
    private static final Proxy.Type TYPE = Proxy.Type.HTTP;

    /**
     * The hostname or IP address of the proxy server
     */
    @NonNull
    private final String hostname;

    /**
     * The port of the proxy server
     */
    @NonNull
    private final Integer port;

    /**
     * The username used to authenticate with the proxy, if applicable
     */
    private final String username;

    /**
     * The password used to authenticate with the proxy, if applicable
     */
    private final char[] password;

    @Getter(lazy = true)
    private final Proxy proxy = new Proxy(TYPE, buildAddress());

    @Getter(lazy = true)
    private final Authenticator authenticator = buildAuthenticator();

    /**
     * Applies this proxy configuration to an OkHttpClient
     *
     * @param builder the builder of the Http Client
     */
    public void apply(OkHttpClient.Builder builder) {
        builder.proxy(getProxy()).proxyAuthenticator(getAuthenticator());
    }

    /**
     * Applies this proxy configuration to a websocket factory's settings
     *
     * @param settings WebSocket's proxy settings
     */
    public void applyWs(ProxySettings settings) {
        settings.setHost(this.hostname)
            .setPort(this.port)
            .setId(this.username)
            .setPassword(password == null ? null : String.valueOf(this.password));
    }

    /**
     * Clears the character array storing the proxy password.
     * Call this method once the proxy is no longer in use to reduce the likelihood of the password being accessible in memory
     */
    public void clearPassword() {
        Arrays.fill(this.password, '0');
    }

    private Authenticator buildAuthenticator() {
        if (username == null && password == null)
            return Authenticator.NONE;

        return (route, response) -> response.request().newBuilder()
            .header("Proxy-Authorization", Credentials.basic(username, String.valueOf(password)))
            .build();
    }

    private SocketAddress buildAddress() {
        return new InetSocketAddress(hostname, port);
    }
}
