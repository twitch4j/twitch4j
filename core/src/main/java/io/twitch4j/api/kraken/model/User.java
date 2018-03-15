package io.twitch4j.api.kraken.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.twitch4j.api.model.IIDModel;
import io.twitch4j.api.model.Model;
import io.twitch4j.api.model.PageInteraction;
import io.twitch4j.api.model.PaginatedList;
import io.twitch4j.auth.ICredential;
import io.twitch4j.enums.Scope;
import io.twitch4j.impl.api.kraken.model.UserBuilder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@JsonDeserialize(builder = UserBuilder.class)
public abstract class User extends Model implements IIDModel<Long> {
    public abstract String getUsername();

    public abstract String getDisplayName();

    public abstract String getBio();

    public abstract String getLogo();

    public abstract String getUserType();

    public abstract Instant createdAt();

    public abstract Instant updatedAt();

    public List<EmoteSet> getEmoteSets() {
        return null;
    }

    public Optional<Subscription> getChannelSubscription(Channel channel) {
        return getChannelSubscription(channel.getId());
    }

    public Optional<Subscription> getChannelSubscription(long channelId) {
        return Optional.empty();
    }

    public PaginatedList<Follow, Integer> getFollows() {
        String url = String.format("/users/%d/follows/channels?limit=100", getId());
        TypeReference<PaginatedList<Follow, Integer>> paginatedFollows = new TypeReference<PaginatedList<Follow, Integer>>() {
        };

        PageInteraction.OffsetPage<Follow> interactivePage = new PageInteraction.OffsetPage<Follow>() {
            @Override
            public PaginatedList<Follow, Integer> invoke(Integer cursor, boolean previous) {
                String uri = String.format("%s&offset=%d", url, cursor);
                return null;
            }
        };
        return null;
    }

    public Optional<Follow> getFollowInfo(Channel channel) {
        return getFollowInfo(channel.getId());
    }

    public Optional<Follow> getFollowInfo(long channelId) {
        String.format("/users/%d/follows/channels/%d", getId(), channelId);
        return Optional.empty();
    }

    public Follow followChannel(Channel channel) {
        return followChannel(channel.getId());
    }

    public Follow followChannel(Channel channel, boolean push) {
        return followChannel(channel.getId(), push);
    }

    public Follow followChannel(long channelId) {
        return followChannel(channelId, false);
    }

    public Follow followChannel(long channelId, boolean push) {
        if (getCredential().isPresent()) {
            ICredential credential = getCredential().get();
            if (credential.getScopes().contains(Scope.USER_FOLLOWS_EDIT)) {
                String url = String.format("/users/%d/subscriptions/%d", getId(), channelId);
            }
        }
        return null;
    }

    public boolean unfollowChannel(Channel channel) {
        return unfollowChannel(channel.getId());
    }

    public boolean unfollowChannel(long channelId) {
        if (getCredential().isPresent()) {
            ICredential credential = getCredential().get();
            if (credential.getScopes().contains(Scope.USER_FOLLOWS_EDIT)) {
                String url = String.format("/users/%d/subscriptions/%d", getId(), channelId);
            }
        }
        return false;
    }

    public PaginatedList<BlockedUser, Integer> getBlockedUsers() {
        if (getCredential().isPresent()) {
            ICredential credential = getCredential().get();
            if (credential.getScopes().contains(Scope.USER_BLOCKS_READ)) {
                String url = String.format("/users/%d/follows/channels?limit=100", getId());
                TypeReference<PaginatedList<BlockedUser, Integer>> paginatedType = new TypeReference<PaginatedList<BlockedUser, Integer>>() {
                };

                PageInteraction.OffsetPage<BlockedUser> interactivePage = new PageInteraction.OffsetPage<BlockedUser>() {
                    @Override
                    public PaginatedList<BlockedUser, Integer> invoke(Integer cursor, boolean previous) {
                        String uri = String.format("%s&offset=%d", url, cursor);
                        return null;
                    }
                };
            }
        }
        return null;
    }

    public BlockedUser blockUser(User user) {
        return blockUser(user.getId());
    }

    public BlockedUser blockUser(long userId) {
        if (getCredential().isPresent()) {
            ICredential credential = getCredential().get();
            if (credential.getScopes().contains(Scope.USER_BLOCKS_EDIT)) {
                String url = String.format("/users/%d/blocks/%d", getId(), userId);
            }
        }
        return null;
    }

    public boolean unblockUser(User user) {
        return unblockUser(user.getId());
    }

    public boolean unblockUser(long userId) {
        if (getCredential().isPresent()) {
            ICredential credential = getCredential().get();
            if (credential.getScopes().contains(Scope.USER_BLOCKS_EDIT)) {
                String url = String.format("/users/%d/blocks/%d", getId(), userId);
            }
        }
        return false;
    }
}
