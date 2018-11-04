package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.domain.ClipList;
import com.github.twitch4j.helix.domain.CreateClip;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@Tag("integration")
public class ClipsServiceTest extends AbtractEndpointTest {

    /**
     * Create Clips
     */
    @Test
    @DisplayName("Create Clip")
    @Disabled
    public void createClipTest() {
        // TestCase
        CreateClip clipData = testUtils.getTwitchHelixClient().createClip(testUtils.getCredential().getAccessToken(), null, null).execute();

        // Validate
    }

    /**
     * Get Clips
     */
    @Test
    @DisplayName("Get Clips")
    public void getClips() {
        // TestCase
        ClipList clipList = testUtils.getTwitchHelixClient().getClips(null, "488552", null, null, null, null, null, null).execute();

        // Validate
        clipList.getData().forEach(clip -> {
            assertNotNull(clip.getId(), "Clips need to have a id.");
        });
    }

}
