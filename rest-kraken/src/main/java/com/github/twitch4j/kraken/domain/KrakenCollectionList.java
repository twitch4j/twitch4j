package com.github.twitch4j.kraken.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@ToString(callSuper = true)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class KrakenCollectionList extends AbstractResultList {
    private List<KrakenCollectionMetadata> collections;
}
