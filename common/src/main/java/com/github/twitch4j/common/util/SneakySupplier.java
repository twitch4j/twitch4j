package com.github.twitch4j.common.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * A supplier that can sneakily throw exceptions.
 * <p>
 * This class should be used sparingly (to avoid hackiness) and carefully (to ensure bubbled exceptions are properly handled).
 *
 * @param <T> the return type of values provided by the supplier
 */
@RequiredArgsConstructor
public final class SneakySupplier<T> implements Supplier<T> {

    /**
     * The action to compute the supplied value, possibly throwing an exception.
     */
    @NotNull
    private final Callable<T> callable;

    @Override
    @SneakyThrows
    public T get() {
        return callable.call();
    }

}
