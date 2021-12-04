package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.AutomodEnforceCheck;
import com.github.twitch4j.helix.domain.AutomodEnforceCheckList;
import com.github.twitch4j.helix.domain.AutomodEnforceStatus;
import com.github.twitch4j.helix.domain.BannedEvent;
import com.github.twitch4j.helix.domain.BannedUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
@Tag("integration")
public class ModerationServiceTest extends AbstractEndpointTest {

    private static final String TWITCH_USER_ID = "149223493";

    @Test
    @DisplayName("Get Banned Users")
    public void getBannedUsers() {
        List<BannedUser> results = TestUtils.getTwitchHelixClient().getBannedUsers(TestUtils.getCredential().getAccessToken(), TWITCH_USER_ID, null, null, null, null)
            .execute()
            .getResults();

        Assertions.assertNotNull(results);
    }

    @Test
    @Disabled
    @DisplayName("Get Banned Events")
    public void getBannedEvents() {
        List<BannedEvent> results = TestUtils.getTwitchHelixClient().getBannedEvents(TestUtils.getCredential().getAccessToken(), TWITCH_USER_ID, null, null, null)
            .execute()
            .getEvents();

        Assertions.assertNotNull(results);
    }

    @Test
    @DisplayName("Check Automod Status")
    public void checkAutomodStatus() {
        List<AutomodEnforceStatus> results = TestUtils.getTwitchHelixClient().checkAutomodStatus(
            TestUtils.getCredential().getAccessToken(),
            TWITCH_USER_ID,
            AutomodEnforceCheckList.builder().message(new AutomodEnforceCheck("Hello world!", "142621956")).build()
        ).execute().getStatuses();

        Assertions.assertNotNull(results);
        Assertions.assertFalse(results.isEmpty());
        Assertions.assertNotNull(results.get(0).getIsPermitted());
    }

}
