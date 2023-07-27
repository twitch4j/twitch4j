package com.github.twitch4j.util.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.net.Proxy;
import java.util.Objects;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Slf4j
public class ProxySpec {
    /**
     * Only HTTP(S) proxies are supported due to library limitations
     */
    @NotNull
    private Proxy.Type type = Proxy.Type.HTTP;

    /**
     * The host of the proxy server
     */
    private String host;

    /**
     * The port of the proxy server
     */
    private Integer port;

    /**
     * The username used to authenticate with the proxy, if applicable
     */
    private String username;

    /**
     * The password used to authenticate with the proxy, if applicable
     */
    private char[] password;

    /**
     * Constructs a validated implementation of {@link ProxySpec}.
     *
     * @param spec the specification to process
     */
    @ApiStatus.Internal
    public ProxySpec(Consumer<ProxySpec> spec) {
        spec.accept(this);
        validate();
    }

    /**
     * Validates the Spec, will throw an exception if required parameters are missing
     *
     * @throws NullPointerException if a required parameter is missing
     * @throws IllegalArgumentException if a parameter has an invalid value
     */
    public void validate() {
        Objects.requireNonNull(type, "type is a required parameter!");
        if (type == Proxy.Type.HTTP) {
            Objects.requireNonNull(host, "host is a required parameter!");
            Objects.requireNonNull(port, "port is a required parameter!");
        }
    }
}
