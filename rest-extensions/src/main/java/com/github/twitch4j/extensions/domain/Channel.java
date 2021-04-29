package com.github.twitch4j.extensions.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class Channel {

    private String id;

    private String username;

    private String game;

    private String title;

    private Long viewCount;

}
