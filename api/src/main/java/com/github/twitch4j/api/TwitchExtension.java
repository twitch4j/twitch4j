package com.github.twitch4j.api;

import feign.InvocationHandlerFactory;
import feign.Target;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * A Twitch4J module for extensions, that modify the behavior of Twitch4J.
 */
@ApiStatus.Internal
public interface TwitchExtension {

    /**
     * Decorates the invocations of a function.
     *
     * @param backendName the name of the backend
     * @param function the function to decorate
     * @param dispatch the dispatch
     * @param target the target
     * @return the decorated method handler
     */
    default ThrowingFunction<Object[], Object, Throwable> decorateFeignInvocation(String backendName, ThrowingFunction<Object[], Object, Throwable> function, Map<Method, InvocationHandlerFactory.MethodHandler> dispatch, Target<?> target) {
        return null;
    }
}
