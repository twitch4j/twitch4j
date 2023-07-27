package com.github.twitch4j.common.feign.capability;

import feign.Contract;
import feign.MethodMetadata;
import feign.Util;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Delegates to the original contract, but replaces the return type for wrapped response types (e.g. {@link CompletableFuture}).
 */
@ApiStatus.Internal
public class TwitchFeignDelegatingContract implements Contract {

    private final Contract delegate;

    public TwitchFeignDelegatingContract(Contract delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<MethodMetadata> parseAndValidateMetadata(Class<?> targetType) {
        List<MethodMetadata> metadataList = this.delegate.parseAndValidateMetadata(targetType);

        for (MethodMetadata metadata : metadataList) {
            Type type = metadata.returnType();

            if (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType().equals(CompletableFuture.class)) {
                metadata.returnType(Util.resolveLastTypeParameter(type, CompletableFuture.class));
            }
        }

        return metadataList;
    }
}
