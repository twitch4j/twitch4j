package com.github.twitch4j.extensions.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor
public class ExtensionSecretList {

    private Integer formatVersion;

    private List<ExtensionSecret> secrets;

}
