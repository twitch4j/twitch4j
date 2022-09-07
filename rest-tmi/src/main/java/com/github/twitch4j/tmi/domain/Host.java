package com.github.twitch4j.tmi.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@Deprecated
public class Host {

    private String hostId;

    private String hostLogin;

    private String hostDisplayName;

    private String targetId;

    private String targetLogin;

    private String targetDisplayName;

    private Boolean hostPartnered;

}
