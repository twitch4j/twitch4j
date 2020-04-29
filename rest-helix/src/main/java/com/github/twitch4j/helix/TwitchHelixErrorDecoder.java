package com.github.twitch4j.helix;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.exception.NotFoundException;
import com.github.twitch4j.common.exception.UnauthorizedException;
import com.github.twitch4j.helix.domain.TwitchHelixError;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TwitchHelixErrorDecoder implements ErrorDecoder {

    // Decoder
    final Decoder decoder;

    // Error Decoder
    final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    // ObjectMapper
    final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Constructor
     *
     * @param decoder Feign Decoder
     */
    public TwitchHelixErrorDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    /**
     * Overwrite the Decode Method to handle custom error cases
     *
     * @param methodKey Method Key
     * @param response Response
     * @return Exception
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        Exception ex = null;

        try {
            String responseBody = response.body() == null ? "" : IOUtils.toString(response.body().asInputStream(), StandardCharsets.UTF_8.name());

            if (response.status() == 401) {
                ex = new UnauthorizedException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("requestHeaders", response.request().headers().entrySet().toString())
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 404) {
                ex = new NotFoundException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("requestHeaders", response.request().headers().entrySet().toString())
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 429) {
                ex = new ContextedRuntimeException("To many requests!")
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("requestHeaders", response.request().headers().entrySet().toString())
                    .addContextValue("responseBody", responseBody);;
            } else if (response.status() == 503) {
                // If you get an HTTP 503 (Service Unavailable) error, retry once.
                // If that retry also results in an HTTP 503, there probably is something wrong with the downstream service.
                // Check the status page for relevant updates.
                ex = new RetryableException(response.status(), "getting service unavailable, retrying ...", Request.HttpMethod.GET, null, response.request());
            } else {
                TwitchHelixError error = objectMapper.readValue(responseBody, TwitchHelixError.class);
                ex = new ContextedRuntimeException("Helix API Error")
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("requestHeaders", response.request().headers().entrySet().toString())
                    .addContextValue("responseBody", responseBody)
                    .addContextValue("errorType", error.getError())
                    .addContextValue("errorStatus", error.getStatus())
                    .addContextValue("errorType", error.getMessage());
            }
        } catch (IOException fallbackToDefault) {
            ex = defaultDecoder.decode(methodKey, response);
        }

        return ex;
    }
}
