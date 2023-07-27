package com.github.twitch4j.common.feign.capability;

import com.github.twitch4j.api.TwitchExtension;
import feign.Capability;
import feign.Contract;
import feign.InvocationHandlerFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ApiStatus.Internal
public class TwitchFeignCapability implements Capability {
    private final String backendName;
    private final List<TwitchExtension> modules;

    public TwitchFeignCapability(@NotNull String backendName, @NotNull List<TwitchExtension> modules) {
        this.backendName = backendName;
        this.modules = modules;
    }

    @Override
    public Contract enrich(Contract contract) {
        return new TwitchFeignDelegatingContract(contract);
    }

    @Override
    public InvocationHandlerFactory enrich(InvocationHandlerFactory invocationHandlerFactory) {
        return (target, dispatch) -> new TwitchFeignInvocationHandler(backendName, target, dispatch, modules);
    }

}
