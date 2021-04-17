package com.github.twitch4j.common.feign.interceptor;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Injects the content-type application/json into all PUT,PATCH,POST requests that didn't specify a content-type
 */
public class JsonContentTypeHeaderInterceptor implements RequestInterceptor {

    /**
     * Holds all http methods that transmit a body (to set the content-type)
     */
    private static final Set<String> methodsWithBody = Stream.of(Request.HttpMethod.POST.name(), Request.HttpMethod.PATCH.name(), Request.HttpMethod.PUT.name()).collect(Collectors.toSet());

    /**
     * Interceptor
     *
     * @param t Feign RequestTemplate
     */
    @Override
    public void apply(RequestTemplate t) {
        // set content-type if not specified in the TwitchKraken interface for POST, PATCH, PUT requests
        if (!t.headers().containsKey("Content-Type") && !t.headers().containsKey("content-type") && methodsWithBody.contains(t.method())) {
            t.header("Content-Type", "application/json");
        }
    }
}
