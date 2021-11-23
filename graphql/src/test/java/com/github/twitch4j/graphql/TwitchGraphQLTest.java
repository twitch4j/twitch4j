package com.github.twitch4j.graphql;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.common.TwitchTestUtils;
import com.github.twitch4j.common.util.EventManagerUtils;
import com.github.twitch4j.graphql.internal.FollowMutation;
import com.github.twitch4j.graphql.internal.UnfollowMutation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

@Slf4j
@Tag("integration")
public class TwitchGraphQLTest {

    private static TwitchGraphQL graphQLClient;

    /**
     * Twitch Client Test
     */
    @Test
    @DisplayName("Tests the TwitchGraphQL Builder")
    @BeforeAll
    public static void buildTwitch4J() {
        // external event manager (for shared module usage - streamlabs4j)
        EventManager eventManager = EventManagerUtils.initializeEventManager(SimpleEventHandler.class);

        // construct twitchClient
        graphQLClient = TwitchGraphQLBuilder.builder()
            .withEventManager(eventManager)
            .build();

        // follow user
        FollowMutation.Data data = graphQLClient.followUser(TwitchTestUtils.getGraphQLCredential(), 24943033l, false).execute();
        System.out.println(data.toString());
    }

    /**
     * Follow User
     */
    @Test
    @DisplayName("Follow a user")
    @Disabled
    public void followUserWithoutNotification() {
        // follow user
        FollowMutation.Data data = graphQLClient.followUser(TwitchTestUtils.getGraphQLCredential(), 24943033l, false).execute();
        System.out.println(data.toString());
    }

    /**
     * Unfollow User
     */
    @Test
    @DisplayName("Unfollow a user")
    @Disabled
    public void unfollowUser() {
        // follow user
        UnfollowMutation.Data data = graphQLClient.unfollowUser(TwitchTestUtils.getGraphQLCredential(), 24943033l).execute();
        System.out.println(data.toString());
    }

}
