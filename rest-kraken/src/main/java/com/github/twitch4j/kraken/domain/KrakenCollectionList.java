package com.github.twitch4j.kraken.domain;

import lombok.*;

import java.util.List;

@Data
@ToString(callSuper = true)
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class KrakenCollectionList extends AbstractResultList {
    private List<KrakenCollectionMetadata> collections;
}
