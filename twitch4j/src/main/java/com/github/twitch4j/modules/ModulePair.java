package com.github.twitch4j.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class ModulePair {

    @Getter
    private final IModule module;

    @Getter
    @Setter
    private boolean active;
}
