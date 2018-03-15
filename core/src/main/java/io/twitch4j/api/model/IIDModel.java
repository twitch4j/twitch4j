package io.twitch4j.api.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

public interface IIDModel<ID extends Serializable> {
    @JsonAlias({
            "_id",
            "slug"
    })
    ID getId();
}
