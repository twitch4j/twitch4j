package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.ChannelSearchResult;
import com.github.twitch4j.helix.domain.Game;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
@Tag("integration")
public class SearchServiceTest extends AbstractEndpointTest {

    @Test
    @DisplayName("Searches for matching categories")
    public void searchCategories() {
        List<Game> resultList = TestUtils.getTwitchHelixClient().searchCategories(TestUtils.getCredential().getAccessToken(), "Tech", null, null).execute().getResults();
        Assertions.assertFalse(resultList.isEmpty());
    }

    @Test
    @DisplayName("Searches for matching channels")
    public void searchChannels() {
        List<ChannelSearchResult> resultList = TestUtils.getTwitchHelixClient().searchChannels(TestUtils.getCredential().getAccessToken(), "twitch4j", 100, null, null).execute().getResults();
        Assertions.assertFalse(resultList.isEmpty());
    }

}
