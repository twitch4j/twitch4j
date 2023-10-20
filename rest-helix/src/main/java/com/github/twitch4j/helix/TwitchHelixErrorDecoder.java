package com.github.twitch4j.helix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.exception.NotFoundException;
import com.github.twitch4j.common.exception.UnauthorizedException;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.helix.domain.TwitchHelixError;
import com.github.twitch4j.helix.interceptor.TwitchHelixRateLimitTracker;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import feign.RetryableException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TwitchHelixErrorDecoder implements ErrorDecoder {

    // Decoder
    final Decoder decoder;

    // Rate Limit Tracker
    final TwitchHelixRateLimitTracker rateLimitTracker;

    // Error Decoder
    final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    // ObjectMapper
    final ObjectMapper objectMapper = TypeConvert.getObjectMapper();

    /**
     * Constructor
     *
     * @param decoder          Feign Decoder
     * @param rateLimitTracker Helix Rate Limit Tracker
     */
    public TwitchHelixErrorDecoder(Decoder decoder, TwitchHelixRateLimitTracker rateLimitTracker) {
        this.decoder = decoder;
        this.rateLimitTracker = rateLimitTracker;
    }

    /**
     * Overwrite the Decode Method to handle custom error cases
     *
     * @param methodKey Method Key
     * @param response  Response
     * @return Exception
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception ex;

        try (InputStream is = response.body() == null ? null : response.body().asInputStream()) {
            String responseBody = is == null ? "" : IOUtils.toString(is, StandardCharsets.UTF_8);

            if (response.status() == 401) {
                ex = new UnauthorizedException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody)
                    .addContextValue("errorStatus", response.status());
            } else if (response.status() == 404) {
                ex = new NotFoundException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody)
                    .addContextValue("errorStatus", response.status());
            } else if (response.status() == 429) {
                ex = new ContextedRuntimeException("Too many requests!")
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody)
                    .addContextValue("errorStatus", response.status());

                // Deplete ban bucket on 429 (to be safe)
                RequestTemplate template = response.request().requestTemplate();
                if (template.path().endsWith("/moderation/bans")) {
                    String channelId = template.queries().get("broadcaster_id").iterator().next();
                    rateLimitTracker.markDepletedBanBucket(channelId);
                }
            } else if (response.status() == 503) {
                // If you get an HTTP 503 (Service Unavailable) error, retry once.
                // If that retry also results in an HTTP 503, there probably is something wrong with the downstream service.
                // Check the status page for relevant updates.
                ex = new RetryableException(response.status(), "getting service unavailable, retrying ...", Request.HttpMethod.GET, (Long) null, response.request());
            } else {
                TwitchHelixError error = objectMapper.readValue(responseBody, TwitchHelixError.class);
                ex = new ContextedRuntimeException("Helix API Error")
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody)
                    .addContextValue("errorType", error.getError())
                    .addContextValue("errorStatus", error.getStatus())
                    .addContextValue("errorType", error.getMessage())
                    .addContextValue("errorMessage", error.getMessage());
            }
        } catch (Exception fallbackToDefault) {
            ex = defaultDecoder.decode(methodKey, response);
        } finally {
            response.close();
        }

        return ex;
    }
}
