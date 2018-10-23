package twitch4j.helix.endpoints;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import twitch4j.helix.domain.ClipList;
import twitch4j.helix.domain.CreateClip;
import twitch4j.helix.domain.ExtensionAnalyticsList;
import twitch4j.helix.domain.GameAnalyticsList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Tag("integration")
public class ClipsTest extends AbtractEndpointTest {

    /**
     * Create Clips
     */
    @Test
    @DisplayName("Create Clip")
    @Disabled
    public void createClipTest() {
        // TestCase
        CreateClip clipData = testUtils.getTwitchHelixClient().createClip(testUtils.getCredential().getAuthToken(), null, null);

        // Validate
    }

    /**
     * Get Clips
     */
    @Test
    @DisplayName("Get Clips")
    public void getClips() {
        // TestCase
        ClipList clipList = testUtils.getTwitchHelixClient().getClips(null, "488552", null, null, null, null, null, null);

        // Validate
        clipList.getData().forEach(clip -> {
            assertNotNull(clip.getId(), "Clips need to have a id.");
        });
    }

}

