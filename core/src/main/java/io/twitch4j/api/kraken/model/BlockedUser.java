package io.twitch4j.api.kraken.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.api.model.IIDModel;
import io.twitch4j.api.model.Model;
import io.twitch4j.impl.api.kraken.model.BlockedUserBuilder;

import java.time.Instant;

@JsonDeserialize(builder = BlockedUserBuilder.class)
public abstract class BlockedUser extends Model implements IIDModel<Long> {
    public abstract Instant updatedAt();

    public abstract User getUser();

    public boolean unblock() {
        return getCredential().isPresent() && getClient().getKrakenApi().userOperation().get(getCredential().get()).unblockUser(getUser());
    }
}
