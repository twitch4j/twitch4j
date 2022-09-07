package com.github.twitch4j.tmi.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class HostList {

    private List<Host> hosts;

}
