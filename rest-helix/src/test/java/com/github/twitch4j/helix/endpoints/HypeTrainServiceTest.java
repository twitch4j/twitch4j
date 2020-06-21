package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.HypeTrainEvent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
@Tag("integration")
public class HypeTrainServiceTest {

    private static final String TWITCH_USER_ID = "149223493";

    @Test
    @DisplayName("Get Hype Train Events")
    public void getHypeTrainEvents() {
        List<HypeTrainEvent> resultList = TestUtils.getTwitchHelixClient().getHypeTrainEvents(TestUtils.getCredential().getAccessToken(), TWITCH_USER_ID, null, null, null).execute().getEvents();
        Assertions.assertTrue(resultList.isEmpty()); // no hype trains available on this account
    }

}
