package com.github.twitch4j.tmi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Host {

    private String hostId;

    private String hostLogin;

    private String hostDisplayName;

    private String targetId;

    private String targetLogin;

    private String targetDisplayName;

    private Boolean hostPartnered;

}
