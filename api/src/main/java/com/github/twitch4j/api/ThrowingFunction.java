package com.github.twitch4j.api;

import lombok.SneakyThrows;

import java.util.function.Function;

/**
 * A functional interface representing a function that may throw a checked exception.
 * <p>
 * This functional interface is similar to the standard {@link java.util.function.Function}
 * interface, but it allows the implementation to throw a checked exception.
 *
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the checked exception that may be thrown by the function
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> extends Function<T, R> {
    R invoke(T t) throws E;

    @Override
    @SneakyThrows
    default R apply(T t) {
        return invoke(t);
    }
}
