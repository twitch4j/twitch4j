package com.github.twitch4j.graphql;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.internal.batch.BatchConfig;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.common.annotation.Unofficial;
import com.github.twitch4j.graphql.command.CommandAcceptFriendRequest;
import com.github.twitch4j.graphql.command.CommandAddChannelBlockedTerm;
import com.github.twitch4j.graphql.command.CommandAddChannelPermittedTerm;
import com.github.twitch4j.graphql.command.CommandApproveUnbanRequest;
import com.github.twitch4j.graphql.command.CommandArchivePoll;
import com.github.twitch4j.graphql.command.CommandBanUser;
import com.github.twitch4j.graphql.command.CommandBulkApproveUnbanRequest;
import com.github.twitch4j.graphql.command.CommandBulkDenyUnbanRequest;
import com.github.twitch4j.graphql.command.CommandCancelPrediction;
import com.github.twitch4j.graphql.command.CommandComputeId;
import com.github.twitch4j.graphql.command.CommandCreateClip;
import com.github.twitch4j.graphql.command.CommandCreateCommunityPointsGoal;
import com.github.twitch4j.graphql.command.CommandCreateModComment;
import com.github.twitch4j.graphql.command.CommandCreatePoll;
import com.github.twitch4j.graphql.command.CommandCreatePrediction;
import com.github.twitch4j.graphql.command.CommandDeleteChannelBlockedTerm;
import com.github.twitch4j.graphql.command.CommandDeleteChannelPermittedTerm;
import com.github.twitch4j.graphql.command.CommandDeleteClips;
import com.github.twitch4j.graphql.command.CommandDeleteCommunityPointsGoal;
import com.github.twitch4j.graphql.command.CommandDenyUnbanRequest;
import com.github.twitch4j.graphql.command.CommandFetchActivePredictions;
import com.github.twitch4j.graphql.command.CommandFetchBanStatus;
import com.github.twitch4j.graphql.command.CommandFetchChatHistory;
import com.github.twitch4j.graphql.command.CommandFetchChatters;
import com.github.twitch4j.graphql.command.CommandFetchCommunityPointsSettings;
import com.github.twitch4j.graphql.command.CommandFetchLastBroadcast;
import com.github.twitch4j.graphql.command.CommandFetchLockedPredictions;
import com.github.twitch4j.graphql.command.CommandFetchModComments;
import com.github.twitch4j.graphql.command.CommandFetchMods;
import com.github.twitch4j.graphql.command.CommandFetchPoll;
import com.github.twitch4j.graphql.command.CommandFetchPrediction;
import com.github.twitch4j.graphql.command.CommandFetchSquadStream;
import com.github.twitch4j.graphql.command.CommandFetchUnbanRequests;
import com.github.twitch4j.graphql.command.CommandFetchUser;
import com.github.twitch4j.graphql.command.CommandFetchUserEmoteSets;
import com.github.twitch4j.graphql.command.CommandFetchUserSubscriptions;
import com.github.twitch4j.graphql.command.CommandFetchVideoComments;
import com.github.twitch4j.graphql.command.CommandFetchVips;
import com.github.twitch4j.graphql.command.CommandFollowUser;
import com.github.twitch4j.graphql.command.CommandLockPrediction;
import com.github.twitch4j.graphql.command.CommandRejectFriendRequest;
import com.github.twitch4j.graphql.command.CommandResolvePrediction;
import com.github.twitch4j.graphql.command.CommandTerminatePoll;
import com.github.twitch4j.graphql.command.CommandUnfollowUser;
import com.github.twitch4j.graphql.command.CommandUpdateClip;
import com.github.twitch4j.graphql.command.CommandUpdateCommunityPointsGoal;
import com.github.twitch4j.graphql.command.CommandUpdateCustomRedemptionStatus;
import com.github.twitch4j.graphql.command.CommandUpdateCustomRedemptionStatuses;
import com.github.twitch4j.graphql.internal.type.CommunityPointsCustomRewardRedemptionStatus;
import com.github.twitch4j.graphql.internal.type.CreateCommunityPointsCommunityGoalInput;
import com.github.twitch4j.graphql.internal.type.CreatePollInput;
import com.github.twitch4j.graphql.internal.type.CreatePredictionEventInput;
import com.github.twitch4j.graphql.internal.type.UnbanRequestStatus;
import com.github.twitch4j.graphql.internal.type.UnbanRequestsSortOrder;
import com.github.twitch4j.graphql.internal.type.UpdateCommunityPointsCommunityGoalInput;
import io.github.xanthic.cache.api.Cache;
import io.github.xanthic.cache.api.domain.ExpiryType;
import io.github.xanthic.cache.core.CacheApi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This is an unofficial API that is not intended for third-party use. Use at your own risk. Methods could change or stop working at any time.
 */
@Slf4j
@Unofficial
@SuppressWarnings("unused")
public class TwitchGraphQL {

    private final TwitchGraphQLConfig config;

    /**
     * Caches an {@link ApolloClient} instance for various access tokens
     */
    private final Cache<String, ApolloClient> clientsByCredential;

    /**
     * Constructor
     *
     * @param configSpec configuration
     */
    public TwitchGraphQL(Consumer<TwitchGraphQLConfig> configSpec) {
        this.config = TwitchGraphQLConfig.process(configSpec);
        this.clientsByCredential = CacheApi.create(spec -> {
            spec.maxSize(64L);
            spec.expiryType(ExpiryType.POST_ACCESS);
            spec.expiryTime(Duration.ofMinutes(5L));
        });
    }

    /**
     * Get ApolloClient with injected
     *
     * @param credential Credential to inject
     * @return ApolloClient
     */
    private ApolloClient getApolloClient(OAuth2Credential credential) {
        if (credential == null) credential = config.defaultToken();
        final String accessToken = credential != null && credential.getAccessToken() != null ? credential.getAccessToken() : "";
        return clientsByCredential.computeIfAbsent(accessToken, s -> {
            // Http Client
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .callTimeout(config.timeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.timeout() / 3, TimeUnit.MILLISECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original
                        .newBuilder()
                        .header("Accept", "*/*")
                        .header("Client-Id", config.clientId())
                        .header("User-Agent", config.userAgent())
                        .header("X-Device-Id", CommandComputeId.INSTANCE.getId());

                    // Apply custom headers
                    config.additionalHeaders().forEach(requestBuilder::header);

                    // Set auth header
                    if (!accessToken.isEmpty()) {
                        requestBuilder.header("Authorization", "OAuth " + accessToken);
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });

            // Apply proxy settings to Http Client
            if (config.proxyConfig() != null)
                config.proxyConfig().apply(clientBuilder);

            // Apollo Client
            return ApolloClient.builder()
                .serverUrl(config.baseUrl())
                .okHttpClient(clientBuilder.build())
                .batchingConfiguration(new BatchConfig(config.batchingEnabled(), 10L, 10))
                .build();
        });
    }

    public CommandFetchBanStatus fetchBanStatus(OAuth2Credential auth, String channelId, String userId) {
        return new CommandFetchBanStatus(getApolloClient(auth), channelId, userId);
    }

    @Deprecated
    public CommandAddChannelBlockedTerm addChannelBlockedTerm(OAuth2Credential auth, String channelId, Boolean isModEditable, List<String> phrases) {
        return new CommandAddChannelBlockedTerm(getApolloClient(auth), channelId, isModEditable, phrases);
    }

    @Deprecated
    public CommandDeleteChannelBlockedTerm deleteChannelBlockedTerm(OAuth2Credential auth, String channelId, List<String> phrases) {
        return new CommandDeleteChannelBlockedTerm(getApolloClient(auth), channelId, phrases);
    }

    public CommandAddChannelPermittedTerm addChannelPermittedTerm(OAuth2Credential auth, String channelId, List<String> phrases) {
        return new CommandAddChannelPermittedTerm(getApolloClient(auth), channelId, phrases);
    }

    public CommandDeleteChannelPermittedTerm deleteChannelPermittedTerm(OAuth2Credential auth, String channelId, List<String> phrases) {
        return new CommandDeleteChannelPermittedTerm(getApolloClient(auth), channelId, phrases);
    }

    public CommandFetchChatHistory fetchChatHistory(OAuth2Credential auth, String channelId, String userId, String after) {
        return new CommandFetchChatHistory(getApolloClient(auth), channelId, userId, after);
    }

    public CommandFetchChatters fetchChatters(OAuth2Credential auth, String channelLogin) {
        return new CommandFetchChatters(getApolloClient(auth), channelLogin);
    }

    public CommandCreateClip createClip(OAuth2Credential auth, String channelId, Double offsetSeconds, String broadcastId, String videoId) {
        return new CommandCreateClip(getApolloClient(auth), channelId, offsetSeconds, broadcastId, videoId);
    }

    public CommandDeleteClips deleteClips(OAuth2Credential auth, List<String> slugs) {
        return new CommandDeleteClips(getApolloClient(auth), slugs);
    }

    public CommandUpdateClip updateClip(OAuth2Credential auth, String slug, String newTitle) {
        return new CommandUpdateClip(getApolloClient(auth), slug, newTitle);
    }

    public CommandFetchCommunityPointsSettings fetchChannelPointRewards(OAuth2Credential auth, String channelLogin) {
        return new CommandFetchCommunityPointsSettings(getApolloClient(auth), channelLogin);
    }

    public CommandCreateCommunityPointsGoal createCommunityPointsGoal(OAuth2Credential auth, CreateCommunityPointsCommunityGoalInput input) {
        return new CommandCreateCommunityPointsGoal(getApolloClient(auth), input);
    }

    public CommandDeleteCommunityPointsGoal deleteCommunityPointsGoal(OAuth2Credential auth, String channelId, String goalId) {
        return new CommandDeleteCommunityPointsGoal(getApolloClient(auth), channelId, goalId);
    }

    public CommandUpdateCommunityPointsGoal updateCommunityPointsGoal(OAuth2Credential auth, UpdateCommunityPointsCommunityGoalInput input) {
        return new CommandUpdateCommunityPointsGoal(getApolloClient(auth), input);
    }

    public CommandUpdateCustomRedemptionStatus updateRedemptionStatus(OAuth2Credential auth, String channelId, String redemptionId, CommunityPointsCustomRewardRedemptionStatus newStatus) {
        return new CommandUpdateCustomRedemptionStatus(getApolloClient(auth), channelId, redemptionId, newStatus);
    }

    public CommandUpdateCustomRedemptionStatuses updateRedemptionStatuses(OAuth2Credential auth, String channelId, List<String> redemptionIds, CommunityPointsCustomRewardRedemptionStatus oldStatus, CommunityPointsCustomRewardRedemptionStatus newStatus) {
        return new CommandUpdateCustomRedemptionStatuses(getApolloClient(auth), channelId, redemptionIds, oldStatus, newStatus);
    }

    public CommandAcceptFriendRequest acceptFriendRequest(OAuth2Credential auth, String targetId) {
        return new CommandAcceptFriendRequest(getApolloClient(auth), targetId);
    }

    public CommandRejectFriendRequest rejectFriendRequest(OAuth2Credential auth, String targetId) {
        return new CommandRejectFriendRequest(getApolloClient(auth), targetId);
    }

    public CommandFetchLastBroadcast fetchLastBroadcast(OAuth2Credential auth, String userId, String userLogin) {
        return new CommandFetchLastBroadcast(getApolloClient(auth), userId, userLogin);
    }

    public CommandCreateModComment createModComment(OAuth2Credential auth, String channelId, String targetId, String text) {
        return new CommandCreateModComment(getApolloClient(auth), channelId, targetId, text);
    }

    public CommandFetchModComments fetchModComments(OAuth2Credential auth, String channelId, String targetId, String after) {
        return new CommandFetchModComments(getApolloClient(auth), channelId, targetId, after);
    }

    public CommandFetchMods fetchMods(OAuth2Credential auth, String channelLogin, String after, Integer limit) {
        return new CommandFetchMods(getApolloClient(auth), channelLogin, after, limit);
    }

    public CommandArchivePoll archivePoll(OAuth2Credential auth, String pollId) {
        return new CommandArchivePoll(getApolloClient(auth), pollId);
    }

    public CommandCreatePoll createPoll(OAuth2Credential auth, CreatePollInput input) {
        return new CommandCreatePoll(getApolloClient(auth), input);
    }

    public CommandFetchPoll fetchPoll(OAuth2Credential auth, String pollId) {
        return new CommandFetchPoll(getApolloClient(auth), pollId);
    }

    public CommandTerminatePoll terminatePoll(OAuth2Credential auth, String pollId) {
        return new CommandTerminatePoll(getApolloClient(auth), pollId);
    }

    public CommandCancelPrediction cancelPrediction(OAuth2Credential auth, String predictionEventId) {
        return new CommandCancelPrediction(getApolloClient(auth), predictionEventId);
    }

    public CommandCreatePrediction createPrediction(OAuth2Credential auth, CreatePredictionEventInput input) {
        return new CommandCreatePrediction(getApolloClient(auth), input);
    }

    public CommandFetchActivePredictions fetchActivePredictions(OAuth2Credential auth, String channelId) {
        return new CommandFetchActivePredictions(getApolloClient(auth), channelId);
    }

    public CommandFetchLockedPredictions fetchLockedPredictions(OAuth2Credential auth, String channelId) {
        return new CommandFetchLockedPredictions(getApolloClient(auth), channelId);
    }

    public CommandFetchPrediction fetchPrediction(OAuth2Credential auth, String predictionEventId) {
        return new CommandFetchPrediction(getApolloClient(auth), predictionEventId);
    }

    public CommandLockPrediction lockPrediction(OAuth2Credential auth, String predictionEventId) {
        return new CommandLockPrediction(getApolloClient(auth), predictionEventId);
    }

    public CommandResolvePrediction resolvePrediction(OAuth2Credential auth, String predictionEventId, String outcomeId) {
        return new CommandResolvePrediction(getApolloClient(auth), predictionEventId, outcomeId);
    }

    public CommandFetchSquadStream fetchSquadStream(OAuth2Credential auth, String id) {
        return new CommandFetchSquadStream(getApolloClient(auth), id);
    }

    public CommandApproveUnbanRequest approveUnbanRequest(OAuth2Credential auth, String id, String message) {
        return new CommandApproveUnbanRequest(getApolloClient(auth), id, message);
    }

    public CommandBulkApproveUnbanRequest bulkApproveUnbanRequest(OAuth2Credential auth, List<String> ids) {
        return new CommandBulkApproveUnbanRequest(getApolloClient(auth), ids);
    }

    public CommandBulkDenyUnbanRequest bulkDenyUnbanRequest(OAuth2Credential auth, List<String> ids) {
        return new CommandBulkDenyUnbanRequest(getApolloClient(auth), ids);
    }

    public CommandDenyUnbanRequest denyUnbanRequest(OAuth2Credential auth, String id, String message) {
        return new CommandDenyUnbanRequest(getApolloClient(auth), id, message);
    }

    public CommandFetchUnbanRequests fetchUnbanRequests(OAuth2Credential auth, String channelLogin, String cursor, Integer limit, UnbanRequestsSortOrder order, UnbanRequestStatus status, String userId) {
        return new CommandFetchUnbanRequests(getApolloClient(auth), channelLogin, cursor, limit, order, status, userId);
    }

    @Deprecated
    public CommandBanUser banUser(OAuth2Credential auth, String channelId, String targetUserLogin, String reason) {
        return new CommandBanUser(getApolloClient(auth), channelId, targetUserLogin, reason);
    }

    public CommandFetchUser fetchUser(OAuth2Credential auth, String userId, String userLogin) {
        return new CommandFetchUser(getApolloClient(auth), userId, userLogin);
    }

    public CommandFetchUserEmoteSets fetchUserEmoteSets(OAuth2Credential auth, String userId) {
        return new CommandFetchUserEmoteSets(getApolloClient(auth), userId);
    }

    public CommandFetchUserSubscriptions fetchUserSubscriptions(OAuth2Credential auth, String userId, Integer first, String after) {
        return new CommandFetchUserSubscriptions(getApolloClient(auth), userId, first, after);
    }

    public CommandFetchVideoComments fetchVideoComments(OAuth2Credential auth, String channelId, String videoId, String id, String after, String before, Integer first, Integer last) {
        return new CommandFetchVideoComments(getApolloClient(auth), channelId, videoId, id, after, before, first, last);
    }

    public CommandFetchVips fetchVips(OAuth2Credential auth, String channelLogin, String after, Integer limit) {
        return new CommandFetchVips(getApolloClient(auth), channelLogin, after, limit);
    }

    /**
     * Follow a user
     *
     * @param auth               Credential
     * @param targetUserId       target user, that the user the auth token was provided for will follow
     * @param goLiveNotification should the follower be notified every time a streamer goes live?
     * @return CommandFollowUser
     */
    public CommandFollowUser followUser(OAuth2Credential auth, Long targetUserId, Boolean goLiveNotification) {
        return new CommandFollowUser(getApolloClient(auth), targetUserId, goLiveNotification);
    }

    /**
     * Unfollow a user
     *
     * @param auth         Credential
     * @param targetUserId target user, that the user the auth token was provided for will follow
     * @return CommandUnfollowUser
     */
    public CommandUnfollowUser unfollowUser(OAuth2Credential auth, Long targetUserId) {
        return new CommandUnfollowUser(getApolloClient(auth), targetUserId);
    }

}
