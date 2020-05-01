package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.ExtensionActiveList;
import com.github.twitch4j.helix.domain.ExtensionList;
import com.github.twitch4j.helix.domain.FollowList;
import com.github.twitch4j.helix.domain.UserList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
            assertTrue(user.getViewCount() > 0, "Views should be grater than 0!");
        });
    }

    /**
     * Get Followers
     */
    @Test
    @DisplayName("Fetch followers")
    public void getFollowers() {
        // TestCase
        FollowList resultList = testUtils.getTwitchHelixClient().getFollowers(testUtils.getCredential().getAccessToken(), twitchUserId, null, null, 100).execute();

        // Test
        assertTrue(resultList.getFollows().size() > 0, "Should at least find one result from the followers method!");
        resultList.getFollows().forEach(follow -> {
            assertNotNull(follow.getFromId(), "FromId should not be null!");
            assertNotNull(follow.getFromName(), "FromName should not be null!");
            assertNotNull(follow.getToId(), "ToId should not be null!");
            assertNotNull(follow.getToName(), "ToName should not be null!");
        });
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
        assertTrue(resultList.getData().getPanels().size() == 3, "Should always get 3 panels!");
        assertTrue(resultList.getData().getOverlays().size() == 1, "Should always get 1 overlay!");
        assertTrue(resultList.getData().getComponents().size() == 2, "Should always get 2 components!");
    }
}

