package com.github.twitch4j.api.util;

import com.github.twitch4j.api.TwitchExtension;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

@ApiStatus.Internal
@ApiStatus.Experimental
public class ExtensionDiscoveryUtils {

    /**
     * Method for locating available extensions, using JDK {@link ServiceLoader} facility, along with module-provided SPI.
     * <p>
     * @implNote This method does not do any caching, so calls should be considered potentially expensive.
     */
    public static List<TwitchExtension> findExtensions() {
        return findExtensions(null);
    }

    /**
     * Method for locating available extensions, using JDK {@link ServiceLoader} facility, along with module-provided SPI.
     * <p>
     * @implNote This method does not do any caching, so calls should be considered potentially expensive.
     */
    public static List<TwitchExtension> findExtensions(ClassLoader classLoader) {
        ArrayList<TwitchExtension> modules = new ArrayList<>();
        ServiceLoader<TwitchExtension> loader = ServiceLoader.load(TwitchExtension.class, classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader);

        for (TwitchExtension extension : loader) {
            modules.add(extension);
        }

        return modules;
    }
}
