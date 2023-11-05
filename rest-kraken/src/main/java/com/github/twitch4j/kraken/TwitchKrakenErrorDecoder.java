package com.github.twitch4j.kraken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.exception.NotFoundException;
import com.github.twitch4j.common.exception.UnauthorizedException;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.kraken.domain.TwitchKrakenError;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @deprecated Kraken is deprecated and has been shut down on <b>Febuary 28, 2022</b>.
 *             More details about the deprecation are available <a href="https://blog.twitch.tv/en/2021/07/15/legacy-twitch-api-v5-shutdown-details-and-timeline">here</a>.
 */
@Slf4j
@Deprecated
public class TwitchKrakenErrorDecoder implements ErrorDecoder {

    // Decoder
    final Decoder decoder;

    // Error Decoder
    final ErrorDecoder defaultDecoder = new Default();

    // ObjectMapper
    final ObjectMapper objectMapper = TypeConvert.getObjectMapper();

    /**
     * Constructor
     *
     * @param decoder Feign Decoder
     */
    public TwitchKrakenErrorDecoder(Decoder decoder) {
        this.decoder = decoder;
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
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 404) {
                ex = new NotFoundException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 429) {
                ex = new ContextedRuntimeException("Too many requests!")
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 503) {
                // If you get an HTTP 503 (Service Unavailable) error, retry once.
                // If that retry also results in an HTTP 503, there probably is something wrong with the downstream service.
                // Check the status page for relevant updates.
                ex = new RetryableException(response.status(), "getting service unavailable, retrying ...", Request.HttpMethod.GET, (Long) null, response.request());
            } else {
                TwitchKrakenError error = objectMapper.readValue(responseBody, TwitchKrakenError.class);
                ex = new ContextedRuntimeException("Kraken API Error")
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody)
                    .addContextValue("errorType", error.getError())
                    .addContextValue("errorStatus", error.getStatus())
                    .addContextValue("errorType", error.getMessage())
                    .addContextValue("errorMessage", error.getMessage());
            }
        } catch (IOException fallbackToDefault) {
            ex = defaultDecoder.decode(methodKey, response);
        }

        return ex;
    }
}
