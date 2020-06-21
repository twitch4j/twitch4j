package com.github.twitch4j.helix.endpoints;

import com.github.twitch4j.helix.TestUtils;
import com.github.twitch4j.helix.domain.ChannelInformation;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

@Slf4j
@Tag("integration")
public class ChannelServiceTest {

    private static final String TWITCH_USER_ID = "149223493";

    @Test
    @DisplayName("Gets channel information")
    public void getChannelInformation() {
        List<ChannelInformation> resultList = TestUtils.getTwitchHelixClient().getChannelInformation(
            TestUtils.getCredential().getAccessToken(),
            Collections.singletonList(TWITCH_USER_ID)
        ).execute().getChannels();

        Assertions.assertFalse(resultList.isEmpty());
    }

    @Test
    @Disabled
    @DisplayName("Modify channel information")
    public void modifyChannelInformation() {
        TestUtils.getTwitchHelixClient().updateChannelInformation(
            TestUtils.getCredential().getAccessToken(), TWITCH_USER_ID,
            new ChannelInformation().withTitle("Hello world!")
        ).execute();
    }

    @Test
    @Disabled
    @DisplayName("Starts a commercial")
    public void startCommercial() {
        TestUtils.getTwitchHelixClient().startCommercial(
            TestUtils.getCredential().getAccessToken(),
            TWITCH_USER_ID,
            30
        ).execute();
    }

}
