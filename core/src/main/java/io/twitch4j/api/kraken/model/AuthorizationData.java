package io.twitch4j.api.kraken.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.enums.Scope;
import io.twitch4j.impl.api.kraken.model.AuthorizationDataBuilder;

import java.time.Instant;
import java.util.Set;

@JsonDeserialize(builder = AuthorizationDataBuilder.class)
public abstract class AuthorizationData {
    public abstract Instant updatedAt();

    public abstract Set<Scope> getScopes();
}
