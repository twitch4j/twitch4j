package com.github.twitch4j.common.spec;

import com.github.twitch4j.api.TwitchExtension;
import com.github.twitch4j.api.util.ExtensionDiscoveryUtils;
import com.github.twitch4j.util.config.ProxySpec;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Data
@Accessors(fluent = true)
@ApiStatus.Experimental
public class ModuleSpec<T> {

    /**
     * Proxy Configuration
     */
    @Nullable
    @Setter(AccessLevel.NONE)
    protected ProxySpec proxy = null;

    /**
     * Extensions to customize the behavior of Twitch4J
     */
    @NotNull
    @Setter(AccessLevel.NONE)
    protected List<TwitchExtension> extensions = new ArrayList<>(2);

    /**
     * Sets the proxy configuration.
     *
     * @param proxySpec the proxy configuration
     * @return the proxy configuration
     */
    public T proxy(Consumer<ProxySpec> proxySpec) {
        this.proxy = new ProxySpec(proxySpec);
        return (T) this;
    }

    /**
     * Registers a module.
     * @param module the module
     */
    public T registerExtension(TwitchExtension module) {
        this.extensions.add(module);
        return (T) this;
    }

    /**
     * Convenience method that will register all extensions found on the classpath
     * <p>
     * Note that method does not do any caching, so calls should be considered potentially expensive.
     *
     * @see ExtensionDiscoveryUtils#findExtensions()
     */
    public T findAndRegisterExtensions() {
        ExtensionDiscoveryUtils.findExtensions().forEach(this::registerExtension);
        return (T) this;
    }
}
