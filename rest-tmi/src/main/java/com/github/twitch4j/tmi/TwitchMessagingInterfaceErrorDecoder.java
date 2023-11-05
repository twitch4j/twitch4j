package com.github.twitch4j.tmi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.twitch4j.common.exception.NotFoundException;
import com.github.twitch4j.common.exception.UnauthorizedException;
import com.github.twitch4j.common.util.TypeConvert;
import com.github.twitch4j.tmi.domain.TMIError;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ContextedRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TwitchMessagingInterfaceErrorDecoder implements ErrorDecoder {

    // Decoder
    final Decoder decoder;

    // Error Decoder
    final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    // ObjectMapper
    final ObjectMapper objectMapper = TypeConvert.getObjectMapper();

    /**
     * Constructor
     *
     * @param decoder Feign Decoder
     */
    public TwitchMessagingInterfaceErrorDecoder(Decoder decoder) {
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
        try (InputStream is = response.body() == null ? null : response.body().asInputStream()) {
            String responseBody = is == null ? "" : IOUtils.toString(is, StandardCharsets.UTF_8);

            if (response.status() == 401) {
                throw new UnauthorizedException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 404) {
                throw new NotFoundException()
                    .addContextValue("requestUrl", response.request().url())
                    .addContextValue("requestMethod", response.request().httpMethod())
                    .addContextValue("responseBody", responseBody);
            } else if (response.status() == 503) {
                // If you get an HTTP 503 (Service Unavailable) error, retry once.
                // If that retry also results in an HTTP 503, there probably is something wrong with the downstream service.
                // Check the status page for relevant updates.
                return new RetryableException(response.status(), "getting service unavailable, retrying ...", Request.HttpMethod.GET, (Long) null, response.request());
            }

            TMIError error = objectMapper.readValue(responseBody, TMIError.class);
            return new ContextedRuntimeException("TMI API Error")
                .addContextValue("requestUrl", response.request().url())
                .addContextValue("requestMethod", response.request().httpMethod())
                .addContextValue("responseBody", responseBody)
                .addContextValue("errorType", error.getError())
                .addContextValue("errorStatus", error.getStatus())
                .addContextValue("errorType", error.getMessage())
                .addContextValue("errorMessage", error.getMessage());
        } catch (IOException fallbackToDefault) {
            return defaultDecoder.decode(methodKey, response);
        }
    }
}
