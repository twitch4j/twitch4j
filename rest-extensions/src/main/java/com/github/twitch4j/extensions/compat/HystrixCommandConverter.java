package com.github.twitch4j.extensions.compat;

import com.netflix.hystrix.HystrixCommand;
import lombok.NonNull;

import java.util.function.Function;

class HystrixCommandConverter<T, U> extends HystrixCommand<U> {

    private final HystrixCommand<T> command;
    private final Function<T, U> converter;

    public HystrixCommandConverter(@NonNull HystrixCommand<T> hystrixCommand, @NonNull Function<T, U> converter) {
        super(hystrixCommand.getCommandGroup());
        this.command = hystrixCommand;
        this.converter = converter;
    }

    @Override
    protected U run() {
        return converter.apply(command.execute());
    }

}
