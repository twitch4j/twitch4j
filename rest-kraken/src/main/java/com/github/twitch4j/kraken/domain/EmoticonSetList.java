package com.github.twitch4j.kraken.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Data
@Setter(AccessLevel.PRIVATE)
public class EmoticonSetList {

    @JsonProperty("emoticon_sets")
    private Map<String, List<EmoticonSetEntry>> emoticonsBySetId;

}
