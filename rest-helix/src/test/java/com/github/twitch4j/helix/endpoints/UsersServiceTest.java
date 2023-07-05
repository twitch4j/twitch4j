package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.BlockedUserList;
import com.github.twitch4j.helix.domain.ExtensionActiveList;
import com.github.twitch4j.helix.domain.ExtensionList;
import com.github.twitch4j.helix.domain.OutboundFollowing;
import com.github.twitch4j.helix.domain.UserList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@Tag("integration")
public class UsersServiceTest extends AbstractEndpointTest {

    // UserId
    private static String twitchUserId = "149223493";

    /**
     * Get Users
     */
    @Test
    @DisplayName("Fetch user information")
    public void getUsers() {
        // TestCase
        UserList resultList = testUtils.getTwitchHelixClient().getUsers(null, null, Arrays.asList("twitch4j")).execute();

        // Test
        assertTrue(resultList.getUsers().size() > 0, "Should at least find one result from the streams method!");
        resultList.getUsers().forEach(user -> {
            assertTrue(user.getId().equals("149223493"), "Twitch4J user id should be 149223493!");
            assertEquals(user.getLogin(), "twitch4j", "Twitch4J user name should be twitch4j!");
            assertEquals(user.getDisplayName(), "twitch4j", "Twitch4J user display name should be twitch4j!");
            assertEquals(user.getType(), "", "Type should be empty!");
            assertEquals(user.getBroadcasterType(), "", "broadcaster-type should be empty!");
            assertEquals(Instant.parse("2017-03-02T12:09:38.184103Z"), user.getCreatedAt());
        });
    }

    /**
     * Get Followers
     */
    @Test
    @DisplayName("Fetch followers")
    public void getFollowers() {
        // TestCase
        OutboundFollowing resultList = testUtils.getTwitchHelixClient().getFollowedChannels(testUtils.getCredential().getAccessToken(), twitchUserId, null, 100, null).execute();

        // Test
        assertTrue(resultList.getTotal() > 0, "Should at least find one result from the followers method!");
        if (resultList.getFollows() != null) {
            resultList.getFollows().forEach(follow -> {
                assertNotNull(follow.getBroadcasterId(), "ToId should not be null!");
                assertNotNull(follow.getBroadcasterLogin(), "ToName should not be null!");
            });
        }
    }

    /**
     * Update user description
     */
    @Test
    @DisplayName("Update the user description")
    public void updateDescription() {
        // TestCase
        UserList resultList = testUtils.getTwitchHelixClient().updateUser(testUtils.getCredential().getAccessToken(), "Twitch4J IntegrationTest User").execute();
    }

    /**
     * Get Extensions
     */
    @Test
    @DisplayName("Get extension list for a specified user")
    public void getExtensionList() {
        // TestCase
        ExtensionList resultList = testUtils.getTwitchHelixClient().getUserExtensions(testUtils.getCredential().getAccessToken()).execute();

        // Test
        assertTrue(resultList.getExtensions().size() > 0, "Should at least find one result from the followers method!");
        resultList.getExtensions().forEach(extension -> {
            assertNotNull(extension.getId(), "Id should not be null!");
            assertNotNull(extension.getName(), "Name should not be null!");
            assertNotNull(extension.getVersion(), "Version should not be null!");
            assertNotNull(extension.getCanActivate(), "CanActivate should not be null!");
            assertNotNull(extension.getType(), "Type should not be null!");
        });
    }

    /**
     * Get Active Extensions
     */
    @Test
    @DisplayName("Get the active extensions for a specified user")
    public void getActiveExtensionList() {
        // TestCase
        ExtensionActiveList resultList = testUtils.getTwitchHelixClient().getUserActiveExtensions(testUtils.getCredential().getAccessToken(), twitchUserId).execute();

        // Test
        assertTrue(resultList.getData().getActivePanels().size() == 3, "Should always get 3 panels!");
        assertTrue(resultList.getData().getActiveOverlays().size() == 1, "Should always get 1 overlay!");
        assertTrue(resultList.getData().getActiveComponents().size() == 2, "Should always get 2 components!");
    }

    @Test
    @DisplayName("Get user block list")
    public void getUserBlockList() {
        BlockedUserList resultList = TestUtils.getTwitchHelixClient().getUserBlockList(TestUtils.getCredential().getAccessToken(), twitchUserId, null, null).execute();
        assertNotNull(resultList);
        assertNotNull(resultList.getBlocks());
    }

    @Test
    @DisplayName("Block user")
    @Disabled
    public void blockUser() {
        assertDoesNotThrow(() -> {
            TestUtils.getTwitchHelixClient().blockUser(TestUtils.getCredential().getAccessToken(), "12427", null, null).execute();
        });
    }

    @Test
    @DisplayName("Unblock user")
    @Disabled
    public void unblockUser() {
        assertDoesNotThrow(() -> {
            TestUtils.getTwitchHelixClient().unblockUser(TestUtils.getCredential().getAccessToken(), "12427").execute();
        });
    }

}
